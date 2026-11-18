package com.beata.wordofthedaybot.bot;

import com.beata.wordofthedaybot.service.DeliveryService;
import com.beata.wordofthedaybot.service.user.UserService;
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

    private final DeliveryService deliveryService;
    private final UserService userService;
    @Value("${telegram.bot.admin-id}")
    private Long adminId;

    public WordOfTheDayBot(
            UserService userService,
            DeliveryService deliveryService) {

        this.userService = userService;
        this.deliveryService = deliveryService;
    }

    @Override
    public void consume(Update update) {
        if (update.hasCallbackQuery()) {
            return;
        }

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

            deliveryService.sendNotification(
                    update.getMessage().getChatId(),
                    "Что-то пошло не так. Попробуйте еще раз"
            );
        }
    }

    private void handlePhotoMessage(Message message) {
        Long tgUserId = message.getFrom().getId();
        Long chatId = message.getChatId();

        if (!tgUserId.equals(adminId)) {
            deliveryService.sendNotification(
                    chatId,
                    "Эта команда доступна только для админа."
            );
            return;
        }
        List<PhotoSize> photos = message.getPhoto();

        String fileId = photos.stream()
                .max(Comparator.comparing(PhotoSize::getFileSize))
            .map(PhotoSize::getFileId)
            .orElseThrow();

        deliveryService.sendNotification(chatId,
                "Telegram file_id:\n" + fileId);
    }

    private void handleStartCommand(Message message) {
        var telegramUser = message.getFrom();

        userService.registerUser(
                telegramUser.getId(),
                telegramUser.getUserName(),
                telegramUser.getFirstName()
        );

       deliveryService.sendNotification(message.getChatId(),
               "Ежедневная рассылка слов включена!");
    }

    private void handleNextCommand(Message message) {
        Long tgUserId = message.getFrom().getId();
        deliveryService.sendNextDelivery(tgUserId);
    }

    private void handleStopCommand(Message message) {
        Long tgUserId = message.getFrom().getId();
        Long chatId = message.getChatId();

        boolean deactivated = userService.deactivateUser(tgUserId);
        if(deactivated) {
            deliveryService.sendNotification(
                    chatId,
                    "Ежедневная рассылка слов отключена. Ее можно возобновить с помощью команды /start"
            );
        } else {
            deliveryService.sendNotification(
                    chatId,
                    "Вы не зарегистрированы. Для регистрации отправьте команду /start"
            );
        }
    }


}
