package dev.whisker.core.monitoring.domain;

import dev.whisker.core.monitoring.infrastructure.files.FileSystemAdapter;
import dev.whisker.core.shared.domain.system.SystemEvent;
import dev.whisker.core.shared.domain.system.SystemEventCodes;
import dev.whisker.core.shared.domain.system.exceptions.DriveRootMonitoringException;
import dev.whisker.core.shared.infrastructure.messaging.rabbit.events.RabbitEventPublisher;
import dev.whisker.core.shared.infrastructure.messaging.rabbit.events.RabbitReceiverTemplate;
import jakarta.annotation.PreDestroy;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static java.nio.file.StandardWatchEventKinds.*;

@Slf4j
@Data
@RequiredArgsConstructor
@Service("projectFileWatcher")
public class FileWatcher {

    private boolean running;
    private WatchService watcher;
    private Path workdir;
    private final RabbitEventPublisher publisher;

    private final Map<Path, Long> pendingChanges = new ConcurrentHashMap<>();
    private final FileSystemAdapter fileSystemAdapter;

    public void start(Path workdir) throws IOException, DriveRootMonitoringException, InterruptedException {
        if (running) {
            log.warn("[WATCHER] Already running");
            SystemEvent error = SystemEvent.ERROR;
            String code = SystemEventCodes.ALREADY_RUNNING.getFullType();

            RabbitReceiverTemplate<Void> event = new RabbitReceiverTemplate<>(code, error, null);
            publisher.publish(event);
            Thread.sleep(1500);
            return;
        }

        this.workdir = workdir.toAbsolutePath();
        if (workdir.getParent() == null) {
            log.error("[WATCHER] Workdir has not been set because parent is null");
            throw new DriveRootMonitoringException(
                    "Cannot monitor root"
            );
        }
        fileSystemAdapter.setup(this.workdir);

        this.watcher = fileSystemAdapter.getWatcher();

        running = true;

        Thread.ofVirtual().start(() -> {
            log.info("[WATCHER] Monitoring {}", this.workdir);

            while (running) {
                try {
                    watchEvents();
                } catch (Exception e) {
                    if (running) {
                        log.error("[WATCHER] Failure", e.getCause());
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


            if (fileSystemAdapter.shouldIgnore(fullPath)) {
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

                fileSystemAdapter.registerDirectory(fullPath);
            }
        }

        if (!key.reset()) {
            log.warn(
                    "[WATCHER] Directory no longer accessible: {}",
                    watchedDirectory
            );
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