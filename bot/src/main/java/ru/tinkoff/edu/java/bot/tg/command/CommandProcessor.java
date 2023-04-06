package ru.tinkoff.edu.java.bot.tg.command;

// This interface assumes that the implementation can process
// given command.

public interface CommandProcessor {
    public static final String UNSUPPORTED_YET = "This command is unsupported yet.";
    public static final String UNSUPPORTED = "This command is unsupported.";

    String process(String command, String text);
}
