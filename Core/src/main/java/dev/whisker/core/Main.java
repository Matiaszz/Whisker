package dev.whisker.core;

import dev.whisker.core.monitoring.domain.AnalysisScheduler;
import dev.whisker.core.monitoring.domain.FileWatcher;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        FileWatcher watcher = new FileWatcher();
        AnalysisScheduler scheduler = new AnalysisScheduler();

        watcher.start();
        scheduler.start();

        Thread.currentThread().join();
    }
}