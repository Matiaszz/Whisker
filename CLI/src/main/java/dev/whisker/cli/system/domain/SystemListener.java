package dev.whisker.cli.system.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import dev.whisker.cli.shared.infrastructure.messaging.RabbitConstants;
import dev.whisker.core.shared.infrastructure.messaging.rabbit.events.RabbitReceiverTemplate;
import picocli.CommandLine;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public class SystemListener {
    private final ConnectionFactory factory = new ConnectionFactory();
    private final ObjectMapper mapper = new ObjectMapper();
    private final String exchange = "system.exchange";
    private final String errorRoutingKey = "system.event.error";
    private final String startedRoutingKey = RabbitConstants.ROUTING_KEY;

    private final CompletableFuture<RabbitReceiverTemplate<?>> startupFuture = new CompletableFuture<>();
    private boolean listening = false;
    private Connection connection;
    private Channel channel;

    public SystemListener() {
        this.factory.setHost("localhost");
        this.factory.setConnectionTimeout(5000);
        this.factory.setHandshakeTimeout(5000);
        this.factory.setThreadFactory(runnable -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t;
        });
        this.mapper.registerModule(new JavaTimeModule());
    }

    public CompletableFuture<RabbitReceiverTemplate<?>> getStartupFuture() {
        return startupFuture;
    }

    public void stop() {
        try {
            if (channel != null && channel.isOpen()) {
                channel.close();
            }
            if (connection != null && connection.isOpen()) {
                connection.close();
            }
        } catch (Exception e) {
            // Silently ignore closure errors
        }
    }

    public void listen() {
        if (listening) return;
        listening = true;

        Thread thread = new Thread(() -> {
            try {
                this.connection = factory.newConnection();
                this.channel = connection.createChannel();

                channel.exchangeDeclare(exchange, "topic", true);
                String queueName = channel.queueDeclare().getQueue();
                channel.queueBind(queueName, exchange, errorRoutingKey);
                channel.queueBind(queueName, exchange, startedRoutingKey);

                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                    String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                    String routingKey = delivery.getEnvelope().getRoutingKey();

                    try {
                        RabbitReceiverTemplate<?> event = mapper.readValue(message, RabbitReceiverTemplate.class);

                        if (routingKey.equals(errorRoutingKey)) {
                            System.out.println("\n" + CommandLine.Help.Ansi.ON.string("@|bold,red [❌] System Error [" + event.code() + "]:|@ " + event.payload()));
                            
                            if (!startupFuture.isDone()) {
                                startupFuture.complete(event);
                            } else {
                                System.out.print(CommandLine.Help.Ansi.ON.string("@|yellow 🐱 Whisker >|@ "));
                            }
                        } else if (routingKey.equals(startedRoutingKey)) {
                            if (!startupFuture.isDone()) {
                                startupFuture.complete(null); // null means success (started)
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("\n[!] Error processing message: " + e.getMessage());
                    }
                };

                channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });

            } catch (Exception e) {
                if (!startupFuture.isDone()) {
                    startupFuture.completeExceptionally(e);
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
}
