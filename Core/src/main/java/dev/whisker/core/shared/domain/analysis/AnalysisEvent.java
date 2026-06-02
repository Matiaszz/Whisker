package dev.whisker.core.shared.domain.analysis;

import dev.whisker.core.shared.domain.event.EventType;

public enum AnalysisEvent implements EventType {
    REQUESTED, STARTED, FINISHED, FAILED;

    @Override
    public String getModel() {
        return "analysis";
    }

    @Override
    public String getAction() {
        return name().toLowerCase();
    }
}
