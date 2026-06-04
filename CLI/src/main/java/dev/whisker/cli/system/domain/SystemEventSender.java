package dev.whisker.cli.system.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.whisker.cli.shared.infrastructure.messaging.BaseWhiskerSendCommand;
import dev.whisker.cli.shared.infrastructure.messaging.EventSender;
import dev.whisker.core.shared.domain.event.SystemCommandType;
import picocli.CommandLine;

import java.nio.file.Paths;
import java.util.Map;

import static dev.whisker.cli.ascii.arts.ASCIICat.wakingCatAscii;
public class SystemEventSender {
    private final boolean debug = true;
    public boolean start(String path) throws JsonProcessingException {
        String currentPath = Paths.get(path).toAbsolutePath().normalize().toString();

        System.out.println(CommandLine.Help.Ansi.ON.string("@|yellow " + wakingCatAscii() + "|@"));
        System.out.println(CommandLine.Help.Ansi.ON.string("@|yellow 🐱 Whisker is waking up...|@"));
        System.out.println(CommandLine.Help.Ansi.ON.string("@|blue Path:|@ " + currentPath));

        BaseWhiskerSendCommand<Map<String, String>> event = new BaseWhiskerSendCommand<>();
        event.build(
                SystemCommandType.START,
                Map.of("path", currentPath)
        );

        EventSender<Map<String, String>> sender = new EventSender<>(
                event,
                null,
                this.debug);
        return sender.send();

    }

    public boolean stop() throws JsonProcessingException {
        System.out.println(CommandLine.Help.Ansi.ON.string("@|yellow " + wakingCatAscii() + "|@"));
        System.out.println(CommandLine.Help.Ansi.ON.string("@|yellow 🐱 Whisker is going to sleep...|@"));

        BaseWhiskerSendCommand<Void> event = new BaseWhiskerSendCommand<>();
        event.build(
                SystemCommandType.STOP,
                null
        );

        EventSender<Void> sender = new EventSender<>(
                event,
                "@|bold,green 💤 Whisker is sleeping!|@",
                this.debug);
        return sender.send();
    }
}
