package dev.whisker.core.shared.infrastructure.messaging.rabbit;

import dev.whisker.core.shared.infrastructure.messaging.BaseWhiskerEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publish(String exchange, String routingKey, BaseWhiskerEvent<?> event) {
        try {
            log.info("[MESSAGING] Publishing event: {}", event.eventType());
            rabbitTemplate.convertAndSend(exchange, routingKey, event);
        } catch (Exception e) {
            log.error("[MESSAGING] Error on event publishing: ", e);
        }
    }
}
