package dev.whisker.core.monitoring.infrastructure.files;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static dev.whisker.core.monitoring.infrastructure.files.WhiskerConstants.DEFAULT_WHISKER_IGNORE;
import static java.nio.file.StandardWatchEventKinds.*;

@Slf4j
@Component
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class FileSystemAdapter {
    private Path workdir;
    private final List<PathMatcher> matchers = new ArrayList<>();
    private WatchService watcher;
    private boolean setup = true;
    private boolean initialized = false;

    public void setup(Path workdir) throws IOException {
        this.workdir = workdir;
        this.watcher = FileSystems.getDefault().newWatchService();
        this.ensureWhiskerIgnoreExists();
        this.loadWhiskerIgnore();
        this.registerDirectory(workdir);
        this.setup = false;
        this.initialized = true;
    }

    public void registerDirectory(Path root) throws IOException {
        if (!isSetup() && !isInitialized()){
            throw new IllegalStateException(
                    "FileSystemAdapter must be initialized before registering directories"
            );
        }
        try (Stream<Path> paths = Files.walk(root)) {

            paths.filter(Files::isDirectory)
                    .filter(path -> !shouldIgnore(path))
                    .forEach(path -> {
                        try {
                            register(path);
                        } catch (IOException e) {
                            log.error(
                                    "[WATCHER] Failed to register {}",
                                    path,
                                    e
                            );
                        }
                    });
        }
    }

    public boolean shouldIgnore(Path path) {
        Path relative;

        String fileName = path.getFileName().toString();
        boolean isIgnorable = fileName.endsWith("~") || fileName.endsWith(".tmp") || fileName.endsWith(".temp");

        if (isIgnorable) {
            return true;
        }

        try {
            relative = workdir.relativize(path);
        } catch (Exception e) {
            return false;
        }

        return matchers.stream()
                .anyMatch(matcher -> matcher.matches(relative));
    }

    private void register(Path dir) throws IOException {

        if (shouldIgnore(dir)) {
            return;
        }

        dir.register(
                watcher,
                ENTRY_CREATE,
                ENTRY_DELETE,
                ENTRY_MODIFY
        );

        log.debug("[WATCHER] Registered {}", dir);
    }

    private void ensureWhiskerIgnoreExists() throws IOException {

        Path whiskerIgnore = workdir.resolve(".whiskerignore");

        if (Files.exists(whiskerIgnore)) {
            return;
        }

        Files.writeString(
                whiskerIgnore,
                DEFAULT_WHISKER_IGNORE,
                StandardOpenOption.CREATE
        );

        log.info("[WATCHER] Created .whiskerignore");
    }

    private void loadWhiskerIgnore() throws IOException {

        matchers.clear();

        Path whiskerIgnore = workdir.resolve(".whiskerignore");

        if (!Files.exists(whiskerIgnore)) {
            return;
        }

        List<String> lines = Files.readAllLines(whiskerIgnore);

        for (String line : lines) {

            line = line.trim();

            if (line.isBlank() || line.startsWith("#")) {
                continue;
            }

            if (line.endsWith("/")) {
                line += "**";
            }

            matchers.add(
                    FileSystems.getDefault()
                            .getPathMatcher("glob:" + line)
            );
        }
    }
}
