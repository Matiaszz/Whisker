package dev.whisker.core.monitoring.domain;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.*;

@Slf4j
@Data
public class FileWatcher {
    private boolean running;
    private WatchService watcher = FileSystems.getDefault().newWatchService();
    private final Path workdir;


    public void start() {
        running = true;

        Thread.ofVirtual().start(() -> {
            while (running) {
                try {
                    log.info("[WATCHER] started");
                    watch();
                } catch (InterruptedException | IOException e) {
                    log.error("[WATCHER] failed");
                    log.error("An error occurred while watching files.");
                    log.error(e.getMessage());
                    this.stop();
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void stop() {
        running = false;
        try {
            watcher.close();
        } catch (IOException e) {
            log.error("Error closing watcher", e);
        }
    }

    private void watch() throws IOException, InterruptedException {
        this.workdir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

        WatchKey key;
        while ((key = watcher.take()) != null) {
            for (WatchEvent<?> event : key.pollEvents()) {
                log.info("[WATCHER] Event kind: {}", event.kind());
                log.info("[WATCHER] Event file: {}", event.context());
                log.info("[WATCHER] Event count: {}", event.count());
            }
            key.reset();
        }
    }

    public FileWatcher(Path workdir) throws IOException {
        this.workdir = workdir;
    }
}
