package com.beata.wordofthedaybot.service;

import com.beata.wordofthedaybot.dto.QuizContent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuizDeliveryService {
    public boolean sendQuiz(Long tgUserId, QuizContent quiz) {
        return true;
    }
}
