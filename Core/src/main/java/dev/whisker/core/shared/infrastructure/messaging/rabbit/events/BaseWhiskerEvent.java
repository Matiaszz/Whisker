package dev.whisker.core.shared.infrastructure.messaging.rabbit.events;

import dev.whisker.core.shared.domain.event.EventType;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
public record BaseWhiskerEvent<T>(
        UUID eventId,
        EventType type,
        OffsetDateTime timestamp,
        String source,
        T payload
) {
}
