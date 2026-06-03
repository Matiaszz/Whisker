package dev.whisker.cli.shared.infrastructure.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import picocli.CommandLine;

import java.nio.charset.StandardCharsets;

import static dev.whisker.cli.shared.infrastructure.messaging.RabbitConstants.EXCHANGE_NAME;
import static dev.whisker.cli.shared.infrastructure.messaging.RabbitConstants.ROUTING_KEY;

public class EventSender<T> {
    private final ObjectMapper mapper = new ObjectMapper();
    private final ConnectionFactory factory = new ConnectionFactory();
    private final String host = "localhost";
    private String successMessage;
    private boolean debug = false;
    private BaseWhiskerEvent<T> event;

    public EventSender(BaseWhiskerEvent<T> event, String message, boolean debug){
        this.successMessage = message;
        this.debug = debug;
        this.event = event;
    }

    public EventSender(BaseWhiskerEvent<T> event, String message){
        this.successMessage = message;
        this.event = event;
    }


    public boolean send() throws JsonProcessingException {

        mapper.registerModule(new JavaTimeModule());
        String jsonEvent = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(event);

        factory.setHost(host); // Em um cenário real, isso poderia ser configurável

        // Tenta desativar logs do SLF4J se possível ou apenas prossegue
        java.util.logging.Logger.getLogger("com.rabbitmq.client").setLevel(java.util.logging.Level.OFF);

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(EXCHANGE_NAME, "topic", true);

            channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, null, jsonEvent.getBytes(StandardCharsets.UTF_8));

            System.out.println(CommandLine.Help.Ansi.ON.string(this.successMessage));
            if (debug){
                System.out.println(CommandLine.Help.Ansi.ON.string("@|faint " + jsonEvent + "|@"));
            }
            return true;
        } catch (Exception e) {
            System.err.println(CommandLine.Help.Ansi.ON.string("@|bold,red ❌ Error connecting to RabbitMQ:|@ " + e.getMessage()));
            return false;
        }
    }

}
