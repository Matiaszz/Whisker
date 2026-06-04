package dev.whisker.core.shared.infrastructure.messaging.rabbit.events;

import dev.whisker.core.shared.domain.event.EventType;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.UUID;

import static dev.whisker.core.shared.infrastructure.messaging.source.PropertySourceResolver.CORE_SOURCE_NAME;

@Builder
public record RabbitReceiverTemplate<T>(
        UUID eventId,
        String code,
        EventType type,
        OffsetDateTime timestamp,
        String source,
        T payload
) {
    public RabbitReceiverTemplate(String code, EventType type, T payload) {
        this(
                UUID.randomUUID(),
                code,
                type,
                OffsetDateTime.now(),
                CORE_SOURCE_NAME,
                payload
        );
    }

    public String getExchange() {
        return type.getExchange();
    }

    public String getRoutingKey() {
        return type.getRoutingKey();
    }

    public String getFullType() {
        return type.getFullType();
    }
}
