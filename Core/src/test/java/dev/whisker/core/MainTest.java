package dev.whisker.core;

import dev.whisker.core.monitoring.domain.AnalysisScheduler;
import dev.whisker.core.monitoring.domain.FileWatcher;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class MainTest {

    @Test
    public void testMainNotInterrupted() throws IOException {
        Path tempDir = Files.createTempDirectory("whisker-test");
        FileWatcher watcher = new FileWatcher(tempDir);
        
        // This should not interrupt the current thread
        assertDoesNotThrow(() -> {
            watcher.start();
        });
        
        // Check if current thread is interrupted
        if (Thread.interrupted()) {
            throw new RuntimeException("Main thread was interrupted by watcher.start()");
        }
    }
}
