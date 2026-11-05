package com.beata.wordofthedaybot.bot;

import com.beata.wordofthedaybot.dto.WordContent;
import com.beata.wordofthedaybot.entity.User;
import com.beata.wordofthedaybot.exception.NoMoreWordsException;
import com.beata.wordofthedaybot.exception.UserNotRegisteredException;
import com.beata.wordofthedaybot.service.UserService;
import com.beata.wordofthedaybot.service.WordDeliveryService;
import com.beata.wordofthedaybot.service.WordMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.Comparator;
import java.util.List;

@Component
public class WordOfTheDayBot implements LongPollingSingleThreadUpdateConsumer {

    private static final Logger log =
            LoggerFactory.getLogger(WordOfTheDayBot.class);

    private final WordDeliveryService wordDeliveryService;
    private final UserService userService;
    private final WordMessageService wordMessageService;
    @Value("${telegram.bot.admin-id}")
    private Long adminId;

    public WordOfTheDayBot(
            UserService userService,
            WordMessageService wordMessageService,
            WordDeliveryService wordDeliveryService,
            @Value("${telegram.bot.token}") String botToken) {

        this.userService = userService;
        this.wordMessageService = wordMessageService;
        this.wordDeliveryService = new WordDeliveryService(botToken);
    }

    @Override
    public void consume(Update update) {

        if (!update.hasMessage()) {
            return;
        }

        Message message = update.getMessage();

        if (message.getPhoto() != null && !message.getPhoto().isEmpty()) {
            handlePhotoMessage(message);
        }

        if (!message.hasText()) {
            return;
        }

        try {
            switch (message.getText()) {
                case "/start" -> handleStartCommand(message);
                case "/next" -> handleNextCommand(message);
                case "/stop" -> handleStopCommand(message);

                default -> log.info("Unknown command: {}", message.getText());
            }
        } catch (Exception e) {
            log.error("Unexpected error while processing Telegram update", e);

            wordDeliveryService.sendNotification(
                    update.getMessage().getChatId(),
                    "Что-то пошло не так. Попробуйте еще раз"
            );
        }
    }

    private void handlePhotoMessage(Message message) {
        Long tgUserId = message.getFrom().getId();
        Long chatId = message.getChatId();

        if (!tgUserId.equals(adminId)) {
            wordDeliveryService.sendNotification(
                    chatId,
                    "This feature is available only for the bot admin."
            );
            return;
        }
        List<PhotoSize> photos = message.getPhoto();

        String fileId = photos.stream()
                .max(Comparator.comparing(PhotoSize::getFileSize))
            .map(PhotoSize::getFileId)
            .orElseThrow();

        wordDeliveryService.sendNotification(chatId,
                "Telegram file_id:\n" + fileId);
    }

    private void handleStartCommand(Message message) {
        var telegramUser = message.getFrom();

        userService.registerUser(
                telegramUser.getId(),
                telegramUser.getUserName(),
                telegramUser.getFirstName()
        );

       wordDeliveryService.sendNotification(message.getChatId(),
               "Ежедневная рассылка слов включена!");
    }

    private void handleNextCommand(Message message) {

        Long tgUserId = message.getFrom().getId();
        Long chatId = message.getChatId();

        try {
            WordContent content = wordMessageService.getNextWord(tgUserId);

            boolean sent = wordDeliveryService.sendWord(chatId, content);
            if (!sent) return;

            wordMessageService.markWordAsSent(
                    tgUserId,
                    content.word().getSequenceNumber()
            );
        } catch (UserNotRegisteredException e) {
            wordDeliveryService.sendNotification(
                    message.getChatId(),
                    "Вы не зарегистрированы. Для регистрации отправьте команду /start "
            );
        } catch (NoMoreWordsException e) {
            wordDeliveryService.sendNotification(
                    message.getChatId(),
                    "Вы достигли конца списка. Новые слова появятся совсем скоро!"
            );
        }
    }

    private void handleStopCommand(Message message) {
        Long tgUserId = message.getFrom().getId();
        Long chatId = message.getChatId();

        boolean deactivated = userService.deactivateUser(tgUserId);
        if(deactivated) {
            wordDeliveryService.sendNotification(
                    chatId,
                    "Ежедневная рассылка слов отключена. Ее можно возобновить с помощью команды /start"
            );
        } else {
            wordDeliveryService.sendNotification(
                    chatId,
                    "Вы не зарегистрированы. Для регистрации отправьте команду /start"
            );
        }
    }


}
