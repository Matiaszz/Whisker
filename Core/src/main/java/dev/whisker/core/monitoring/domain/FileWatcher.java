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

        this.stop();
    }


    public void stop() {
        Thread.currentThread().interrupt();
        running = false;
    }

    private void watch() throws IOException, InterruptedException {
            this.workdir.register(watcher, ENTRY_CREATE);
            this.workdir.register(watcher, ENTRY_MODIFY);
            this.workdir.register(watcher, ENTRY_DELETE);

            WatchKey key;
            while ((key = watcher.take()) != null) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    log.info("[WATCHER] Event kind: {}", event.kind());
                    log.info("[WATCHER] Event file: {}", event.context());
                }
                key.reset();
            }

    }

    public FileWatcher(Path workdir) throws IOException {
        this.workdir = workdir;
    }
}
