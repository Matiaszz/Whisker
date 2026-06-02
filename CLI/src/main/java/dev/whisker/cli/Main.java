package dev.whisker.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import dev.whisker.cli.domain.WhiskerEvent;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Help.Ansi;

import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.Callable;

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
        System.out.println(Ansi.ON.string("@|bold,yellow " + getBanner() + "|@"));
        CommandLine.usage(this, System.out);
        return 0;
    }

    @Command(name = "start", description = "Starts code analysis in the current directory")
    static class StartCommand implements Callable<Integer> {

        private static final String EXCHANGE_NAME = "system.exchange";
        private static final String ROUTING_KEY = "system.started";

        @Override
        public Integer call() throws Exception {
            String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
            
            System.out.println(Ansi.ON.string("@|yellow " + getWakingCat() + "|@"));
            System.out.println(Ansi.ON.string("@|yellow 🐱 Whisker is waking up...|@"));
            System.out.println(Ansi.ON.string("@|blue Path:|@ " + currentPath));

            WhiskerEvent event = WhiskerEvent.builder()
                    .eventType("system.start")
                    .source("whisker-cli")
                    .payload(Map.of("path", currentPath))
                    .build();

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            String jsonEvent = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(event);

            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost"); // Em um cenário real, isso poderia ser configurável
            
            // Tenta desativar logs do SLF4J se possível ou apenas prossegue
            java.util.logging.Logger.getLogger("com.rabbitmq.client").setLevel(java.util.logging.Level.OFF);

            try (Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel()) {

                channel.exchangeDeclare(EXCHANGE_NAME, "topic", true);

                channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, null, jsonEvent.getBytes(StandardCharsets.UTF_8));
                
                System.out.println(Ansi.ON.string("@|bold,green ✅ Whisker is ready!|@"));
                System.out.println(Ansi.ON.string("@|faint " + jsonEvent + "|@"));
            } catch (Exception e) {
                System.err.println(Ansi.ON.string("@|bold,red ❌ Error connecting to RabbitMQ:|@ " + e.getMessage()));
                return 1;
            }

            return 0;
        }

        private static String getWakingCat() {
            return """
                    (:`--..___...-''``-._             |`._
                      ```--...--.      . `-..__      .`/ _\\ \s
                                `\\     '       ```--`.    />
                                : :   :               `:`-'
                                 `.:.  `.._--...___     ``--...__     \s
                                    ``--..,)       ```----....__,)
                """;
        }
    }

    private static String getBanner() {
        return """
                   __..--''``---....___   _..._    __
         /// //_.-'    .-/";  `        ``<._  ``.''_ `. / // /
        ///_.-' _..--.'_    \\\\                  `( ) ) // //
        / (_..-' // (<  _   ;_..__               ; `' / ///
         / // // / / `-._,_)' // / ``--...____..-' /// / //
        
                          W H I S K E R
                      Code Analysis Platform
             \s""";
    }
}
