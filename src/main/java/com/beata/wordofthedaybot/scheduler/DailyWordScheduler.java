package com.beata.wordofthedaybot.scheduler;

import com.beata.wordofthedaybot.entity.user.User;
import com.beata.wordofthedaybot.service.DeliveryService;
import com.beata.wordofthedaybot.service.user.UserService;
import com.beata.wordofthedaybot.service.word.WordMessageService;
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
    private final DeliveryService deliveryService;

//    @Scheduled(cron = "0 0 12 * * *", zone = "Europe/Moscow")
    @Scheduled(cron = "*/10 * * * * *", zone = "Europe/Moscow")
    public void sendDailyContent() {
        log.info("Daily word scheduler started");
        List<User> users = userService.getAllActiveUsers();
        log.info("Active users found: {}", users.size());

        for (User user : users) {
            Long tgUserId = user.getTgUserId();
            deliveryService.sendNextDelivery(tgUserId);
        }

        log.info("Daily word scheduler finished");
    }



}
