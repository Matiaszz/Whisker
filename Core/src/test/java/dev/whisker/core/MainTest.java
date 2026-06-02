package dev.whisker.core;

import dev.whisker.core.monitoring.domain.FileWatcher;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class MainTest {

    @Test
    public void testMainNotInterrupted() throws IOException {
        Path tempDir = Files.createTempDirectory("whisker-test");
        FileWatcher watcher = new FileWatcher(tempDir);
    }
}
