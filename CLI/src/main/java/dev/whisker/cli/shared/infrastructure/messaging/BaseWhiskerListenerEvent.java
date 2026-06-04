package dev.whisker.cli.shared.infrastructure.messaging;

import java.time.OffsetDateTime;
import java.util.UUID;

public record BaseWhiskerListenerEvent<T>(
        UUID eventId,
        String code,
        String eventType,
        OffsetDateTime timestamp,
        String source,
        T payload
) {
}

