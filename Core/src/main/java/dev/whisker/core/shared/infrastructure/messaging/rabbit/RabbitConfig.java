package dev.whisker.core.shared.infrastructure.messaging.rabbit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String SYSTEM_EXCHANGE = "system.exchange";
    public static final String SYSTEM_START_QUEUE = "system.start.queue";
    public static final String SYSTEM_START_ROUTING_KEY = "system.start";

    public static final String SYSTEM_STOP_QUEUE = "system.stop.queue";
    public static final String SYSTEM_STOP_ROUTING_KEY = "system.stop";

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public TopicExchange systemExchange() {
        return new TopicExchange(SYSTEM_EXCHANGE);
    }

    @Bean
    public Queue systemStartQueue() {
        return new Queue(SYSTEM_START_QUEUE, true);
    }

    @Bean
    public Queue systemStopQueue(){
        return new Queue(SYSTEM_STOP_QUEUE, true);
    }

    @Bean
    public Binding systemStartBinding(Queue systemStartQueue, TopicExchange systemExchange) {
        return BindingBuilder.bind(systemStartQueue).to(systemExchange).with(SYSTEM_START_ROUTING_KEY);
    }


    @Bean
    public Binding systemStopBinding(Queue systemStopQueue, TopicExchange systemExchange) {
        return BindingBuilder.bind(systemStopQueue).to(systemExchange).with(SYSTEM_STOP_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jacksonConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
