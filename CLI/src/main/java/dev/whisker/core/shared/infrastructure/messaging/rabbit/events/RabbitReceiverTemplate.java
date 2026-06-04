package dev.whisker.core.shared.infrastructure.messaging.rabbit.events;

import dev.whisker.core.shared.domain.event.EventType;

import java.time.OffsetDateTime;
import java.util.UUID;

import static dev.whisker.core.shared.infrastructure.messaging.source.PropertySourceResolver.CLI_SOURCE_NAME;

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
                CLI_SOURCE_NAME,
                payload
        );
    }
}
