package dev.whisker.core.shared.domain.system;

import dev.whisker.core.shared.domain.event.EventType;

public enum SystemCommand implements EventType {
    START, STOP;

    @Override
    public String getExchange() {
        return "system.command";
    }

    @Override
    public String getRoutingKey() {
        return name().toLowerCase();
    }
}
