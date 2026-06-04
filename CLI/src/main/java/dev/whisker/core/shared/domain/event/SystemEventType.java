package dev.whisker.core.shared.domain.event;

public enum SystemEventType implements EventType {
    STARTED, STOPPED, NOTIFICATION, ERROR;

    @Override
    public String getExchange() {
        return "system.event";
    }

    @Override
    public String getRoutingKey() {
        return this.name().toLowerCase();
    }
}
