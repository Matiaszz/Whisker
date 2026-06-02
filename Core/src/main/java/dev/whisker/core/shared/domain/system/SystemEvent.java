package dev.whisker.core.shared.domain.system;

import dev.whisker.core.shared.domain.event.EventType;

public enum SystemEvent implements EventType {
    START, STOPPED, ERROR, NOTIFICATION;

    @Override
    public String getModel() {
        return "analysis";
    }

    @Override
    public String getAction() {
        return name().toLowerCase();
    }
}
