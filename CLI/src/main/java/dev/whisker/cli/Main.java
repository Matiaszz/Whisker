package dev.whisker.cli;

import dev.whisker.cli.system.domain.SystemEventSender;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Help.Ansi;

import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

import static dev.whisker.cli.ascii.arts.ASCIICat.*;

@Command(name = "whisker", mixinStandardHelpOptions = true, version = "whisker 0.0.1", description = "Whisker Code Analysis CLI", subcommands = {
        Main.StartCommand.class })
public class Main implements Callable<Integer> {

    public static void main(String[] args) {
        System.setProperty("logback.statusListenerClass", "ch.qos.logback.core.status.NopStatusListener");
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() {
        System.out.println(Ansi.ON.string("@|bold,yellow " + whiskerBannerAscii() + "|@"));
        CommandLine.usage(this, System.out);
        return 0;
    }

    @Command(name = "start", description = "Starts code analysis in the current directory")
    static class StartCommand implements Callable<Integer> {

        private final SystemEventSender systemEventSender = new SystemEventSender();
        private final AtomicBoolean stopped = new AtomicBoolean(false);

        private void stopGracefully() {
            if (stopped.compareAndSet(false, true)) {
                try {
                    boolean stopped = systemEventSender.stop();
                    if (!stopped) {
                        throw new IllegalStateException("Cannot stop code analysis");
                    }
                } catch (Exception e) {
                    System.err.println("[❌] Failed to stop Whisker: " + e.getMessage());
                }
            }
        }

        @Override
        public Integer call() throws Exception {

            String path = ".";

            boolean started = systemEventSender.start(path);

            if (!started) {
                return 1;
            }

            System.out.println(Ansi.ON.string("@|bold,green ✅ Whisker is ready!|@"));

            Runtime.getRuntime().addShutdownHook(
                    new Thread(this::stopGracefully));

            Scanner scanner = new Scanner(System.in);
            boolean running = true;

            while (running) {

                System.out.print(
                        Ansi.ON.string("@|yellow 🐱 Whisker >|@ "));
                System.out.flush();

                String command;

                try {
                    command = scanner.nextLine().trim();
                } catch (Exception e) {
                    break;
                }

                switch (command.toLowerCase()) {

                    case "quit":
                    case "exit":
                        stopGracefully();
                        running = false;
                        break;

                    default:
                        System.out.println(
                                Ansi.ON.string(
                                        "@|red Unknown command:|@ " + command));
                }
            }
            scanner.close();

            return 0;
        }
    }
}
