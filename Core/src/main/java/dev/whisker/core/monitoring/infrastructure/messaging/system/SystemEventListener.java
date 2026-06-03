package dev.whisker.core.monitoring.infrastructure.messaging.system;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.whisker.core.monitoring.domain.FileWatcher;
import dev.whisker.core.shared.infrastructure.messaging.source.Source;
import dev.whisker.core.shared.infrastructure.messaging.source.PropertySourceResolver;
import dev.whisker.core.shared.domain.system.payloads.StartPayload;
import dev.whisker.core.shared.infrastructure.messaging.BaseWhiskerEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

@Slf4j
@Component
@RequiredArgsConstructor
public class SystemEventListener {

    private final FileWatcher fileWatcher;
    private final PropertySourceResolver propertySourceResolver;

    @RabbitListener(queues = "system.start.queue")
    public void handleSystemStart(BaseWhiskerEvent<StartPayload> event) throws IOException {
        log.info("📩 [LISTENER] Message received from system.start.queue: {}", event.eventId());

        try {
            String rawSource = event.source().trim();
            Source source = propertySourceResolver.resolve(rawSource);

            if (source == Source.CORE){
                throw new IllegalArgumentException("Only CLI or Desktop clients can start monitoring");
            }
            StartPayload payload = event.payload();
            if (payload == null) {
                log.error("Payload null");
                throw new IllegalArgumentException("Payload must be not null");
            }
            String workdir = payload.path();

            this.fileWatcher.start(
                    Path.of(workdir)
            );

        } catch (IllegalArgumentException | IOException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @RabbitListener(queues = "system.stop.queue")
    public void handleSystemStop(BaseWhiskerEvent<StartPayload> event) throws JsonProcessingException {
        log.info("📩 [LISTENER] Message received from system.stop.queue: {}", event.eventId());

        try {
            Source source = propertySourceResolver.resolve(event.source());
            if (source == Source.CORE) {
                throw new IllegalArgumentException();
            }

            this.fileWatcher.stop();

        } catch(IllegalArgumentException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
