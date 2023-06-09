package ru.tinkoff.edu.java.bot;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.tinkoff.edu.java.bot.tg.command.HelpCommandProcessor;
import ru.tinkoff.edu.java.bot.tg.command.TrackCommandProcessor;
import ru.tinkoff.edu.java.bot.tg.command.UntrackCommandProcessor;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommandProcessorTest {
    @Test
    void check_help_processor_Test() {
        var helpProcessor = new HelpCommandProcessor();
        assertEquals(HelpCommandProcessor.HELP_MSG, helpProcessor.process("help", "/help", 0));
    }

    @ParameterizedTest
    @MethodSource("badUrlStringSource")
    void check_bad_url_track_processor_Test(String urlString) {
        var trackProcessor = new TrackCommandProcessor();
        assertEquals(TrackCommandProcessor.BAD_URL, trackProcessor.process("track",
                String.format("/track %s", urlString), 0));
    }

    @Test
    void check_no_url_track_processor_Test() {
        var trackProcessor = new TrackCommandProcessor();
        assertEquals(TrackCommandProcessor.NO_URL, trackProcessor.process("track", "/track", 0));
    }

    @ParameterizedTest
    @MethodSource("badUrlStringSource")
    void check_bad_url_untrack_processor_Test(String urlString) {
        var trackProcessor = new UntrackCommandProcessor();
        assertEquals(UntrackCommandProcessor.BAD_URL, trackProcessor.process("untrack",
                String.format("/untrack %s", urlString), 0));
    }

    @Test
    void check_no_url_untrack_processor_Test() {
        var trackProcessor = new UntrackCommandProcessor();
        assertEquals(UntrackCommandProcessor.NO_URL, trackProcessor.process("untrack", "/untrack", 0));
    }

    static Stream<String> badUrlStringSource() {
        return Stream.of("httpslocalhost:8080",
                "htps:",
                "heelowwjkjfdsl",
                "htts:/https://",
                "http//localhost:8080");
    }
}
