package com.beata.wordofthedaybot.config;

import com.beata.wordofthedaybot.bot.WordOfTheDayBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

@Configuration
public class TelegramBotConfig {

    @Bean(destroyMethod = "close")
    public TelegramBotsLongPollingApplication telegramBotsApplication() {
        return new TelegramBotsLongPollingApplication();
    }

    @Bean
    public CommandLineRunner registerBot(
            TelegramBotsLongPollingApplication botsApplication,
            WordOfTheDayBot bot,
            @Value("${telegram.bot.token}") String botToken) {

        return args ->
            botsApplication.registerBot(botToken, bot);
    }
}
