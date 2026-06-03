package dev.whisker.cli.system.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.whisker.cli.shared.infrastructure.messaging.BaseWhiskerEvent;
import dev.whisker.cli.shared.infrastructure.messaging.EventSender;
import picocli.CommandLine;

import java.nio.file.Paths;
import java.util.Map;

import static dev.whisker.cli.ascii.arts.ASCIICat.wakingCatAscii;
public class SystemEvent {
    private final boolean debug = true;
    public boolean start(String path) throws JsonProcessingException {
        String currentPath = Paths.get(path).toAbsolutePath().normalize().toString();

        System.out.println(CommandLine.Help.Ansi.ON.string("@|yellow " + wakingCatAscii() + "|@"));
        System.out.println(CommandLine.Help.Ansi.ON.string("@|yellow 🐱 Whisker is waking up...|@"));
        System.out.println(CommandLine.Help.Ansi.ON.string("@|blue Path:|@ " + currentPath));

        BaseWhiskerEvent<Map<String, String>> event = new BaseWhiskerEvent<>();
        event.build(
                "system.start",
                Map.of("path", currentPath)
        );

        EventSender<Map<String, String>> sender = new EventSender<>(
                event,
                "@|bold,green ✅ Whisker is ready!|@",
                this.debug);
        return sender.send();

    }

    public boolean stop() throws JsonProcessingException {
        System.out.println(CommandLine.Help.Ansi.ON.string("@|yellow " + wakingCatAscii() + "|@"));
        System.out.println(CommandLine.Help.Ansi.ON.string("@|yellow 🐱 Whisker is going to sleep...|@"));

        BaseWhiskerEvent<Void> event = new BaseWhiskerEvent<>();
        event.build(
                "system.stop",
                null
        );

        EventSender<Void> sender = new EventSender<>(
                event,
                "@|bold,green 💤 Whisker is sleeping!|@",
                this.debug);
        return sender.send();
    }
}
