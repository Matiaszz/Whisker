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

    // Pega todos os métodos que implementam RabbitConfigTemplate no projeto
    @Bean
    public Declarables dynamicDeclarables(List<RabbitConfigTemplate> templates) {
        // Declarable = em vez de fazer bean por bean pra declarar uma nova fila e etc,
        // ele já upa todos de uma vez
        List<Declarable> declarables = new ArrayList<>();

        for (RabbitConfigTemplate template : templates) {
            TopicExchange exchange = new TopicExchange(template.getExchangeName());
            TopicExchange dlxExchange = new TopicExchange(template.getExchangeName() + ".dlx");
            declarables.add(exchange);
            declarables.add(dlxExchange);

            for (EventType event : template.getEvents()) {
                String routingKey = event.getFullType();
                String queueName = routingKey + ".queue";
                String dlqName = routingKey + ".dlq";
                String dlxName = template.getExchangeName() + ".dlx";

                // Tratamento de erros runtime, estudar melhor sobre dlq
                Map<String, Object> args = new HashMap<>();
                args.put("x-dead-letter-exchange", dlxName);
                args.put("x-dead-letter-routing-key", dlqName);

                Queue queue = new Queue(queueName, true, false, false, args);
                Binding binding = BindingBuilder.bind(queue)
                        .to(exchange)
                        .with(routingKey);

                declarables.add(queue);
                declarables.add(binding);

                Queue dlq = new Queue(dlqName, true, false, false);
                Binding dlqBinding = BindingBuilder.bind(dlq).to(dlxExchange).with(dlqName);

                declarables.add(dlq);
                declarables.add(dlqBinding);
            }
        }

        return new Declarables(declarables);
    }
}
