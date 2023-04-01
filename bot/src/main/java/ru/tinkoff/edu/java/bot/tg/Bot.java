package ru.tinkoff.edu.java.bot.tg;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Bot implements AutoCloseable {
    private final TelegramBot bot;
    private final MessageProcessor messageProcessor;

    @Autowired
    public Bot(@Qualifier("tgBot") TelegramBot bot,
               MessageProcessor messageProcessor) {
        this.bot = bot;
        this.messageProcessor = messageProcessor;
    }

    @PostConstruct
    void init() {
        // TODO: make async processing for each request
        bot.setUpdatesListener(updates -> {
            for (var update: updates) {
                if (update.message() == null)
                    continue;

                SendMessage answer = messageProcessor.process(update);
                bot.execute(answer);
            }

            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });

        log.info("Bot has been initialized.");
    }

    @Override
    public void close() throws Exception {
        bot.shutdown();
    }
}
