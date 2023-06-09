package ru.tinkoff.edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;

// TODO: make preconfigured messaged centralized.

@Slf4j
@Configuration
@PropertySource("classpath:secrets.properties")
@EnableAsync
public class BotConfig {
    @Bean
    public String botApiKey(@Value("${secrets.bot_api_key}") String key) {
        return key;
    }

    @Bean
    public TelegramBot tgBot(@Qualifier("botApiKey") String key) {
        var bot = new TelegramBot(key);
        return bot;
    }
}
