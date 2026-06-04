package dev.whisker.core.shared.infrastructure.messaging.rabbit.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publish(RabbitReceiverTemplate<?> event) {
        try {
            log.info("[MESSAGING] Publishing event: {}", event.getFullType());
            rabbitTemplate.convertAndSend(event.getExchange(), event.getRoutingKey(), event);
        } catch (Exception e) {
            log.error("[MESSAGING] Error on event publishing: ", e);
        }
    }
}
