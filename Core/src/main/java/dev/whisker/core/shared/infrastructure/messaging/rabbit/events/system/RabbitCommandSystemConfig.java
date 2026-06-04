package dev.whisker.core.shared.infrastructure.messaging.rabbit.events.system;

import dev.whisker.core.shared.domain.event.EventType;
import dev.whisker.core.shared.domain.system.SystemCommand;
import dev.whisker.core.shared.infrastructure.messaging.rabbit.RabbitConfigTemplate;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitCommandSystemConfig implements RabbitConfigTemplate {
    @Override
    public String getExchangeName() {
        return "system.command";
    }

    @Override
    public EventType[] getEvents() {
        return SystemCommand.values();
    }
}
