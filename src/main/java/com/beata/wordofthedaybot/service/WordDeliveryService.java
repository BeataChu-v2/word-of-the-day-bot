package com.beata.wordofthedaybot.service;

import com.beata.wordofthedaybot.dto.WordContent;
import com.beata.wordofthedaybot.entity.Image;
import com.beata.wordofthedaybot.entity.Word;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Service
@Slf4j
public class WordDeliveryService {

    private final TelegramClient telegramClient;

    public WordDeliveryService(
            @Value("${telegram.bot.token}") String botToken) {
        this.telegramClient = new OkHttpTelegramClient(botToken);
    }

    public boolean sendWord(Long chatId, WordContent content) {
        if (!content.images().isEmpty()) {
            Image image = content.images().getFirst();
            if (!sendPhoto(chatId, content.word().getWord(), image)) {
                return false;
            }
        }

        String message = formatWordMessage(content);
        return sendText(chatId, message);
    }


    private String formatWordMessage(WordContent content) {
        StringBuilder text = new StringBuilder();
        Word word = content.word();
        text.append("#wordoftheday ")
                .append(word.getSequenceNumber())
                .append("\n\n")
                .append("Word: ")
                .append(word.getWord().toUpperCase())
                .append(" - ")
                .append(word.getTranslation())
                .append("\n\n");

        text.append("Collocations:\n");

        content.collocations().forEach(collocation ->
                text.append("• ")
                        .append(collocation.getText())
                        .append(" — ")
                        .append(collocation.getTranslation())
                        .append("\n")
        );

        text.append("\nPhrases:\n");
        content.phrases().forEach(phrase ->
                text.append("• ")
                        .append(phrase.getText())
                        .append(" — ")
                        .append(phrase.getTranslation())
                        .append("\n")
        );
        return text.toString();
    }

    private boolean sendText(Long chatId, String text) {
        SendMessage response = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();

        try {
            telegramClient.execute(response);
            return true;
        } catch (TelegramApiException e) {
            log.error("Failed to send Telegram message to chat {}", chatId, e);
            sendText(
                    chatId,
                    "Не смогли отправить текст. Если ошибка повторится, обратитесь к владельцу бота"
            );
            return false;
        }
    }

    private boolean sendPhoto(Long chatId, String word, Image image) {
        SendPhoto response = SendPhoto.builder()
                .chatId(chatId)
                .photo(new InputFile(image.getFileId()))
                .caption(word)
                .build();
        try {
            telegramClient.execute(response);
            return true;
        } catch (TelegramApiException e) {
            log.error("Failed to send image to chat {}", chatId, e);
            sendText(
                    chatId,
                    "Не смогли отправить картинку. Если ошибка повторится, обратитесь к владельцу бота"
            );
            return false;
        }
    }

    public void sendNotification(Long chatId, String text) {
        SendMessage response = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
        try {
            telegramClient.execute(response);
        } catch (TelegramApiException e) {
            log.error("Failed to send Telegram notification to chat {}", chatId, e);
        }
    }
}
