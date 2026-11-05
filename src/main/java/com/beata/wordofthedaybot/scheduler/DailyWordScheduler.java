package com.beata.wordofthedaybot.scheduler;

import com.beata.wordofthedaybot.dto.WordContent;
import com.beata.wordofthedaybot.entity.User;
import com.beata.wordofthedaybot.exception.NoMoreWordsException;
import com.beata.wordofthedaybot.service.UserService;
import com.beata.wordofthedaybot.service.WordDeliveryService;
import com.beata.wordofthedaybot.service.WordMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DailyWordScheduler {
    private final UserService userService;
    private final WordMessageService wordMessageService;
    private final WordDeliveryService wordDeliveryService;

//    @Scheduled(cron = "0 0 12 * * *", zone = "Europe/Moscow")
    @Scheduled(cron = "*/10 * * * * *", zone = "Europe/Moscow")
    public void sendWordsDaily() {
        log.info("Daily word scheduler started");
        List<User> users = userService.getAllActiveUsers();
        log.info("Active users found: {}", users.size());

        for (User user : users) {
            Long tgUserId = user.getTgUserId();

            try {
                WordContent content = wordMessageService.getNextWord(tgUserId);
                boolean sent = wordDeliveryService.sendWord(
                        tgUserId,
                        content
                );

                if (!sent) {
                    log.warn(
                            "Word was not delivered to user {}",
                            tgUserId
                    );
                }

                wordMessageService.markWordAsSent(
                        tgUserId,
                        content.word().getSequenceNumber()
                );
            } catch (NoMoreWordsException e) {
                log.info(
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

        log.info("Daily word scheduler finished");
    }

}
