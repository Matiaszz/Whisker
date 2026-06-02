package dev.whisker.core.monitoring.domain;

import jakarta.annotation.PreDestroy;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static java.nio.file.StandardWatchEventKinds.*;

@Slf4j
@Data
@NoArgsConstructor
@Component("projectFileWatcher")
public class FileWatcher {

    private static final String DEFAULT_WHISKER_IGNORE = """
            # Dependencies
            node_modules/
            vendor/
            .venv/
            venv/
            __pycache__/

            # Git
            .git/

            # IDEs
            .idea/
            .vscode/
            .settings/

            # Java
            target/
            .gradle/
            build/
            out/

            # Flutter / Dart
            .dart_tool/
            .flutter-plugins
            .flutter-plugins-dependencies

            # Front-end
            .next/
            dist/
            coverage/

            # Logs
            *.log

            # Lock files
            package-lock.json
            yarn.lock
            pnpm-lock.yaml
            pubspec.lock

            # Binaries
            *.exe
            *.dll
            *.so
            *.dylib
            *.jar
            *.war

            # System
            .DS_Store
            Thumbs.db
            """;

    private boolean running;
    private WatchService watcher;
    private Path workdir;

    private final List<PathMatcher> matchers = new ArrayList<>();
    private final Map<Path, Long> pendingChanges = new ConcurrentHashMap<>();

    public void start(Path workdir) throws IOException {

        if (running) {
            log.warn("[WATCHER] Already running");
            return;
        }

        this.workdir = workdir.toAbsolutePath();

        ensureWhiskerIgnoreExists();
        loadWhiskerIgnore();

        this.watcher = FileSystems.getDefault().newWatchService();

        registerAll(this.workdir);

        running = true;

        Thread.ofVirtual().start(() -> {
            log.info("[WATCHER] Monitoring {}", this.workdir);

            while (running) {
                try {
                    watchEvents();
                } catch (Exception e) {
                    if (running) {
                        log.error("[WATCHER] Failure", e);
                    }
                }
            }
        });

        Thread.ofVirtual().start(() -> {
            while (running) {
                flushPendingChanges();
            }
        });
    }

    @PreDestroy
    public void stop() {

        running = false;

        try {
            if (watcher != null) {
                watcher.close();
            }
        } catch (IOException e) {
            log.error("[WATCHER] Failed to close WatchService", e);
        }
    }

    private void watchEvents()
            throws IOException, InterruptedException {

        WatchKey key = watcher.take();

        Path watchedDirectory = (Path) key.watchable();

        for (WatchEvent<?> event : key.pollEvents()) {

            if (event.kind() == OVERFLOW) {
                continue;
            }

            Path relativePath = (Path) event.context();

            Path fullPath =
                    watchedDirectory.resolve(relativePath);


            if (shouldIgnore(fullPath)) {
                continue;
            }

            if (!Files.isDirectory(fullPath)) {
                pendingChanges.put(
                        fullPath,
                        System.currentTimeMillis()
                );
            }

            if (event.kind() == ENTRY_CREATE
                    && Files.isDirectory(fullPath)) {

                registerAll(fullPath);
            }
        }

        if (!key.reset()) {
            log.warn(
                    "[WATCHER] Directory no longer accessible: {}",
                    watchedDirectory
            );
        }
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

    private boolean shouldIgnore(Path path) {

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

    private void registerAll(Path root) throws IOException {
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

    private void flushPendingChanges() {
        long now = System.currentTimeMillis();

        pendingChanges.entrySet().removeIf(entry -> {

            if (now - entry.getValue() < 1000) {
                return false;
            }

            Path file = entry.getKey();

            log.info("[WATCHER] Change detected -> {}", file);

            // publicar evento para análise local

            return true;
        });
    }
}