package dev.whisker.core.monitoring.infrastructure.files;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
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
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class FileSystemAdapter {
    private Path workdir;
    private final List<PathMatcher> matchers = new ArrayList<>();
    private final WatchService watcher;

    public void setup(Path workdir) throws IOException {
        this.ensureWhiskerIgnoreExists();
        this.loadWhiskerIgnore();
        this.registerDirectory(workdir);
        this.workdir = workdir;
    }

    public void registerDirectory(Path root) throws IOException {
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
