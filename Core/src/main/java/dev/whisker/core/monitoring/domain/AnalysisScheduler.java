package dev.whisker.core.monitoring.domain;

public class AnalysisScheduler {
    private volatile boolean running;

    public void start() {
        running = true;

        Thread.ofVirtual().start(() -> {
            System.out.println("[SCHEDULER] iniciado");

            while (running) {
                try {
                    Thread.sleep(10000);

                    System.out.println(
                            "[SCHEDULER] aguardando alterações..."
                    );

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }

            System.out.println("[SCHEDULER] finalizado");
        });
    }

    public void stop() {
        running = false;
    }
}
