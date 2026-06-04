package dev.whisker.cli.shared.infrastructure.messaging;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.whisker.core.shared.domain.event.EventType;
import dev.whisker.core.shared.infrastructure.messaging.source.PropertySourceResolver;

import java.time.OffsetDateTime;
import java.util.UUID;

public class BaseWhiskerSendCommand<T> {
    private UUID eventId;
    private EventType eventType;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime timestamp;
    
    private String source;
    private T payload;

    public BaseWhiskerSendCommand() {
        this.eventId = UUID.randomUUID();
        this.timestamp = OffsetDateTime.now();
        this.source = PropertySourceResolver.CLI_SOURCE_NAME;
    }

    // Getters and Setters
    public UUID getEventId() { return eventId; }
    public void setEventId(UUID eventId) { this.eventId = eventId; }
    public EventType getEventType() { return eventType; }
    public void setEventType(EventType eventType) { this.eventType = eventType; }
    public OffsetDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(OffsetDateTime timestamp) { this.timestamp = timestamp; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public T getPayload() { return payload; }
    public void setPayload(T payload) { this.payload = payload; }

    public void build(EventType eventType, T payload){
        this.eventType = eventType;
        this.payload = payload;
    }

}
