package dev.whisker.core.shared.infrastructure.messaging.rabbit.events.analysis;

import dev.whisker.core.shared.domain.analysis.AnalysisEvent;
import dev.whisker.core.shared.domain.event.EventType;
import dev.whisker.core.shared.infrastructure.messaging.rabbit.RabbitConfigTemplate;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitAnalysisConfig implements RabbitConfigTemplate {
    @Override
    public String getExchangeName() {
        return "analysis.exchange";
    }

    @Override
    public EventType[] getEvents() {
        return AnalysisEvent.values();
    }
}
