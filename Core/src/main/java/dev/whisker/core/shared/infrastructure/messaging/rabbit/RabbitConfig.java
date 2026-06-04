package dev.whisker.core.shared.infrastructure.messaging.rabbit;

import dev.whisker.core.shared.domain.event.EventType;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class RabbitConfig {

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Declarables dynamicDeclarables(List<RabbitConfigTemplate> templates) {

        List<Declarable> declarables = new ArrayList<>();

        for (RabbitConfigTemplate template : templates) {

            String exchangeName = template.getExchangeName();
            String dlxName = exchangeName + ".dlx";

            TopicExchange exchange = new TopicExchange(exchangeName);
            TopicExchange dlxExchange = new TopicExchange(dlxName);

            declarables.add(exchange);
            declarables.add(dlxExchange);

            for (EventType event : template.getEvents()) {

                String routingKey = event.getRoutingKey();

                String queueName = event.getFullType() + ".queue";
                String dlqName = event.getFullType() + ".dlq";

                Map<String, Object> args = new HashMap<>();
                args.put("x-dead-letter-exchange", dlxName);
                args.put("x-dead-letter-routing-key", dlqName);

                Queue queue = new Queue(
                        queueName,
                        true,
                        false,
                        false,
                        args
                );

                Binding binding = BindingBuilder
                        .bind(queue)
                        .to(exchange)
                        .with(routingKey);

                Queue dlq = new Queue(
                        dlqName,
                        true
                );

                Binding dlqBinding = BindingBuilder
                        .bind(dlq)
                        .to(dlxExchange)
                        .with(dlqName);

                declarables.add(queue);
                declarables.add(binding);
                declarables.add(dlq);
                declarables.add(dlqBinding);
            }
        }

        return new Declarables(declarables);
    }
}