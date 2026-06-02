package dev.whisker.core.shared.infrastructure.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
public record BaseWhiskerEvent<T>(
        UUID eventId,
        String eventType,
        OffsetDateTime timestamp,
        String source,
        T payload
) {

    private static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();

    public String toJson() throws JsonProcessingException {
        return MAPPER.writeValueAsString(this);
    }

    //cu

    public static <T> BaseWhiskerEvent<T> fromJson(
            String json,
            Class<T> payloadType
    ) throws JsonProcessingException {

        return MAPPER.readValue(
                json,
                MAPPER.getTypeFactory()
                        .constructParametricType(
                                BaseWhiskerEvent.class,
                                payloadType
                        )
        );
    }
}
