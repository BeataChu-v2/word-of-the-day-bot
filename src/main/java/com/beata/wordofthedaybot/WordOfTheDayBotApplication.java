package com.beata.wordofthedaybot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WordOfTheDayBotApplication {

    static void main(String[] args) {
        SpringApplication.run(WordOfTheDayBotApplication.class, args);
    }
}