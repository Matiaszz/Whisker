package dev.whisker.core.shared.domain.analysis;

import dev.whisker.core.shared.domain.event.EventType;

public enum AnalysisEvent implements EventType {
    REQUESTED, STARTED, FINISHED, FAILED;

    @Override
    public String getExchange() {
        return "analysis.exchange";
    }

    @Override
    public String getRoutingKey() {
        return name().toLowerCase();
    }
}
