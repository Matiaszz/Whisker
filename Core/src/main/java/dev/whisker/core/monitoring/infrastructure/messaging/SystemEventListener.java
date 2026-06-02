package dev.whisker.core.monitoring.infrastructure.messaging;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.whisker.core.monitoring.domain.FileWatcher;
import dev.whisker.core.shared.domain.system.SystemEvent;
import dev.whisker.core.shared.domain.system.payloads.StartPayload;
import dev.whisker.core.shared.infrastructure.messaging.BaseWhiskerEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

@Slf4j
@Component
@RequiredArgsConstructor
public class SystemEventListener {

    private final FileWatcher fileWatcher;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "system.start.queue")
    public void handleSystemStart(String json) throws IOException {
        log.info("📩 [LISTENER] Message received from system.start.queue: {}", json);
        BaseWhiskerEvent<StartPayload> event =
                BaseWhiskerEvent.fromJson(
                        json,
                        StartPayload.class
                );
        this.fileWatcher.start(Path.of(event.payload().path()));
    }
}
