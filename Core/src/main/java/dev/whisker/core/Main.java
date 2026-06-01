package dev.whisker.core;

import dev.whisker.core.monitoring.domain.AnalysisScheduler;
import dev.whisker.core.monitoring.domain.FileWatcher;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws RuntimeException {

        Path path = Paths.get("E:\\testesCodigos");

        FileWatcher watcher = new FileWatcher(path);
        AnalysisScheduler scheduler = new AnalysisScheduler();

        watcher.start();
        scheduler.start();

        Thread.currentThread().join();
    }
}