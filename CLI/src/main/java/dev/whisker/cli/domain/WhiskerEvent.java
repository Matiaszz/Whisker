package dev.whisker.cli.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
public class WhiskerEvent {
    @Builder.Default
    private String eventId = UUID.randomUUID().toString();
    
    private String eventType;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    @Builder.Default
    private OffsetDateTime timestamp = OffsetDateTime.now();
    
    @Builder.Default
    private String source = "whisker-cli";
    
    private Map<String, Object> payload;
}
