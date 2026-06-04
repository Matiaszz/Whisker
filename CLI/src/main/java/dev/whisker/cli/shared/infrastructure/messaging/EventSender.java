package dev.whisker.cli.shared.infrastructure.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import picocli.CommandLine;

import java.nio.charset.StandardCharsets;

public class EventSender<T> {
    private final ObjectMapper mapper = new ObjectMapper();
    private final ConnectionFactory factory = new ConnectionFactory();
    private final String host = "localhost";
    private final String successMessage;
    private boolean debug = false;
    private final BaseWhiskerSendCommand<T> event;
    private final String routingKey;
    private final String exchange;

    public EventSender(BaseWhiskerSendCommand<T> event, String message, boolean debug){
        this.successMessage = message;
        this.debug = debug;
        this.event = event;
        this.routingKey = event.getEventType().getRoutingKey();
        this.exchange = event.getEventType().getExchange();
    }

    public EventSender(BaseWhiskerSendCommand<T> event, String message){
        this.successMessage = message;
        this.event = event;
        this.routingKey = event.getEventType().getRoutingKey();
        this.exchange = event.getEventType().getExchange();
    }


    public boolean send() throws JsonProcessingException {

        mapper.registerModule(new JavaTimeModule());
        if (debug){

            System.out.println(CommandLine.Help.Ansi.ON.string("@|faint " + event.getEventType().getFullType()));
        }
        String jsonEvent = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(event);

        factory.setHost(host); // Num cenário real, isso poderia ser configurável
        factory.setConnectionTimeout(5000); // 5 seconds timeout
        factory.setHandshakeTimeout(5000);
        factory.setThreadFactory(runnable -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t;
        });

        // Tenta desativar ‘logs’ do SLF4J se possível ou apenas prossegue
        java.util.logging.Logger.getLogger("com.rabbitmq.client").setLevel(java.util.logging.Level.OFF);

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(exchange, "topic", true);

            channel.basicPublish(exchange, routingKey, null, jsonEvent.getBytes(StandardCharsets.UTF_8));

            if (this.successMessage != null) {
                System.out.println(CommandLine.Help.Ansi.ON.string(this.successMessage));
            }
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
