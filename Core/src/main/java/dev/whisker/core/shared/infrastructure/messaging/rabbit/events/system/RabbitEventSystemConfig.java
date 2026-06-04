package dev.whisker.core.shared.infrastructure.messaging.rabbit.events.system;

import dev.whisker.core.shared.domain.event.EventType;
import dev.whisker.core.shared.domain.system.SystemEvent;
import dev.whisker.core.shared.infrastructure.messaging.rabbit.RabbitConfigTemplate;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitEventSystemConfig implements RabbitConfigTemplate {
    @Override
    public String getExchangeName() {
        return "system.event";
    }

    @Override
    public EventType[] getEvents() {
        return SystemEvent.values();
    }
}
