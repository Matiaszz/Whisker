package dev.whisker.cli.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public class WhiskerEvent {
    private String eventId;
    private String eventType;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime timestamp;
    
    private String source;
    private Map<String, Object> payload;

    public WhiskerEvent() {
        this.eventId = UUID.randomUUID().toString();
        this.timestamp = OffsetDateTime.now();
        this.source = "whisker-cli";
    }

    // Getters and Setters
    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public OffsetDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(OffsetDateTime timestamp) { this.timestamp = timestamp; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public Map<String, Object> getPayload() { return payload; }
    public void setPayload(Map<String, Object> payload) { this.payload = payload; }

    // Manual Builder to keep compatibility with Main.java
    public static WhiskerEventBuilder builder() {
        return new WhiskerEventBuilder();
    }

    public static class WhiskerEventBuilder {
        private String eventType;
        private String source = "whisker-cli";
        private Map<String, Object> payload;

        public WhiskerEventBuilder eventType(String eventType) {
            this.eventType = eventType;
            return this;
        }

        public WhiskerEventBuilder source(String source) {
            this.source = source;
            return this;
        }

        public WhiskerEventBuilder payload(Map<String, Object> payload) {
            this.payload = payload;
            return this;
        }

        public WhiskerEvent build() {
            WhiskerEvent event = new WhiskerEvent();
            event.setEventType(this.eventType);
            event.setSource(this.source);
            event.setPayload(this.payload);
            return event;
        }
    }
}
