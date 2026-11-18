package com.beata.wordofthedaybot.service;

import com.beata.wordofthedaybot.dto.QuizContent;
import com.beata.wordofthedaybot.dto.WordContent;
import com.beata.wordofthedaybot.exception.MessageLengthExceededException;
import com.beata.wordofthedaybot.exception.NoMoreWordsException;
import com.beata.wordofthedaybot.service.quiz.QuizMessageService;
import com.beata.wordofthedaybot.service.user.UserService;
import com.beata.wordofthedaybot.service.word.WordMessageService;
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
public class DeliveryService {

    private final TelegramClient telegramClient;
    private final WordMessageService wordMessageService;
    private final QuizMessageService quizMessageService;
    private final TelegramHtmlFormatter telegramHtmlFormatter;
    private final UserService userService;

    public DeliveryService(
            @Value("${telegram.bot.token}") String botToken,
            WordMessageService wordMessageService,
            QuizMessageService quizMessageService,
            TelegramHtmlFormatter telegramHtmlFormatter,
            UserService userService
    ) {
        this.telegramClient = new OkHttpTelegramClient(botToken);
        this.wordMessageService = wordMessageService;
        this.quizMessageService = quizMessageService;
        this.telegramHtmlFormatter = telegramHtmlFormatter;
        this.userService = userService;
    }

    public void sendNextDelivery(Long tgUserId) {
        try {
            if (quizMessageService.isQuizDue(tgUserId)) {
                deliverQuiz(tgUserId);
            } else {
                deliverWord(tgUserId);
            }
        } catch (NoMoreWordsException e) {
            sendNotification(tgUserId, "Вы достигли конца списка. Ежедневная рассылка приостановлена.");
            userService.deactivateUser(tgUserId);
            log.warn(
                    "No more words available for user {}",
                    tgUserId
            );
        } catch (Exception e) {
            log.error(
                    "Failed to send daily word to user {}",
                    tgUserId,
                    e
            );
        }
    }

    private void deliverWord(Long tgUserId) {
        WordContent content = wordMessageService.getNextWord(tgUserId);
        boolean sent = sendWord(
                tgUserId,
                content
        );

        if (!sent) {
            log.warn(
                    "Word was not delivered to user {}",
                    tgUserId
            );
        } else {
            wordMessageService.markWordAsSent(
                    tgUserId,
                    content.word().getSequenceNumber()
            );
        }
    }

    private void deliverQuiz(Long tgUserId) {
        QuizContent content = quizMessageService.getQuizForUser(tgUserId);
        boolean sent = sendQuiz(
                tgUserId,
                content
        );

        if (!sent) {
            log.warn("Quiz was not delivered to user {}", tgUserId);
        } else {
            quizMessageService.markQuizSent(tgUserId);
            log.info("Quiz successfully sent to user {}", tgUserId);
        }
    }

    private boolean sendWord(Long chatId, WordContent content) {
        String text = telegramHtmlFormatter.formatWordMessage(content);

        // if there is no photo, just send plain text
        if (content.word().getImageFileId() == null) {
            log.warn("No image found for word {}", content.word().getWord());
            return sendText(chatId, text);
        }

        //if there is a photo, send the word in one or two messages
        if (text.length() < 1024) {
            return sendPhoto(chatId, text, content.word().getImageFileId());
        } else if (text.length() < 4096) {
            sendPhoto(chatId, content.word().getWord(), content.word().getImageFileId());
            return sendText(chatId, text);
        } else {
            log.warn("Message length exceeded for word {}", content.word().getWord());
            throw new MessageLengthExceededException();
        }
    }

    private boolean sendQuiz(Long chatId, QuizContent content) {
        String text = telegramHtmlFormatter.formatQuizMessage(content);

        // if there is no photo, just send plain text
        if (content.quiz().getImageFileId() == null) {
            log.warn("No image found for quiz {}", content.quiz().getId());
            return sendText(chatId, text);
        }

        if (text.length() < 4096) {
            sendPhoto(chatId, "Weekly quiz", content.quiz().getImageFileId());
            return sendText(chatId, text);
        } else {
            log.warn("Message length exceeded for quiz {}", content.quiz().getId());
            throw new MessageLengthExceededException();
        }
    }

    private boolean sendText(Long chatId, String text) {
        SendMessage response = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .parseMode("HTML")
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

    private boolean sendPhoto(Long chatId, String caption, String imageFileId) {
        SendPhoto response = SendPhoto.builder()
                .chatId(chatId)
                .photo(new InputFile(imageFileId))
                .caption(caption)
                .parseMode("HTML")
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
            log.warn("Failed to send Telegram notification to chat {}", chatId, e);
        }
    }
}
