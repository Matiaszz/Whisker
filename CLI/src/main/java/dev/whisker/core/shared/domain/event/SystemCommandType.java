package dev.whisker.core.shared.domain.event;

public enum SystemCommandType implements EventType {
    START, STOP;

    @Override
    public String getExchange() {
        return "system.command";
    }

    @Override
    public String getRoutingKey() {
        return this.name().toLowerCase();
    }
}
