package dev.whisker.core.shared.infrastructure.messaging.rabbit;

import dev.whisker.core.shared.domain.event.EventType;

public interface RabbitConfigTemplate {
    String getExchangeName();
    EventType[] getEvents();
}
