package dev.whisker.core.monitoring.domain;


public class FileWatcher {
    private boolean running;

    public void start() {
        running = true;

        Thread.ofVirtual().start(() -> {
            System.out.println("[WATCHER] iniciado");

            while (running) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }

            System.out.println("[WATCHER] finalizado");
        });
    }


    public void stop() {
        running = false;
    }
}
