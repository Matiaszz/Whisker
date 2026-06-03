package dev.whisker.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import dev.whisker.cli.shared.infrastructure.messaging.BaseWhiskerEvent;
import dev.whisker.cli.system.domain.SystemEvent;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Help.Ansi;

import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Callable;

import static dev.whisker.cli.ascii.arts.ASCIICat.*;

@Command(name = "whisker", mixinStandardHelpOptions = true, version = "whisker 1.0",
        description = "Whisker Code Analysis CLI",
        subcommands = {Main.StartCommand.class})
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

        private final SystemEvent systemEvent = new SystemEvent();

        @Override
        public Integer call() throws Exception {
            String path = ".";
            boolean started = systemEvent.start(path);

            Scanner scanner = new Scanner(System.in);
            boolean running = true;

            while (running && started) {
                System.out.print(Ansi.ON.string("@|yellow 🐱  Whisker >|@"));

                String command = scanner.nextLine();

                switch (command) {
                    case "quit":
                        systemEvent.stop();
                        running = false;
                }
            }

            return 0;
        }
    }
}
