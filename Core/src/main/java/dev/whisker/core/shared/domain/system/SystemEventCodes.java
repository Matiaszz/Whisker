package dev.whisker.core.shared.domain.system;

import dev.whisker.core.shared.domain.event.EventType;

public enum SystemEventCodes implements EventType {
    STARTED, STOPPED, ALREADY_RUNNING, NOT_RUNNING;

    @Override
    public String getExchange() {
        return "system";
    }

    @Override
    public String getRoutingKey() {
        return this.name();
    }
}
