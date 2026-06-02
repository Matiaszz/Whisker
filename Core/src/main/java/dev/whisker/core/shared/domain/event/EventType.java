package dev.whisker.core.shared.domain.event;

public interface EventType {
    String getModel();
    String getAction();

    default String getFullType(){
        return getModel() + "." + getAction();
    }

}
