package dev.whisker.core.shared.domain.event;

public interface EventType {
    String getExchange();
    String getRoutingKey();

    default String getFullType() {
        return getExchange() + "." + getRoutingKey();
    }
}
