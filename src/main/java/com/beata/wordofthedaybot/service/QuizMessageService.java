package com.beata.wordofthedaybot.service;

import com.beata.wordofthedaybot.dto.QuizContent;
import com.beata.wordofthedaybot.entity.Quiz;
import com.beata.wordofthedaybot.entity.QuizExercise;
import com.beata.wordofthedaybot.entity.User;
import com.beata.wordofthedaybot.entity.UserProgress;
import com.beata.wordofthedaybot.exception.QuizNotFoundException;
import com.beata.wordofthedaybot.exception.UserNotRegisteredException;
import com.beata.wordofthedaybot.exception.UserProgressNotFound;
import com.beata.wordofthedaybot.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizMessageService {
    private final UserRepository userRepository;
    private final QuizRepository quizRepository;
    private final UserProgressRepository userProgressRepository;
    private final QuizExerciseRepository quizExerciseRepository;

    @Transactional(readOnly = true)
    public boolean isQuizDue(Long tgUserId) {
        UserProgress progress = getUserProgress(tgUserId);
        Integer lastSequenceNumber = progress.getLastSequenceNumber();
        Integer lastQuizSequenceNumber = progress.getLastQuizSequenceNumber();

        return lastSequenceNumber >= 6
                && lastSequenceNumber % 6 == 0
                && lastQuizSequenceNumber < lastSequenceNumber;
    }

    @Transactional(readOnly = true)
    public QuizContent getQuizForUser(Long tgUserId) {
        UserProgress progress = getUserProgress(tgUserId);
        Integer toSequenceNumber = progress.getLastSequenceNumber();
        Integer fromSequenceNumber = toSequenceNumber - 5;
        Quiz quiz = quizRepository
                .findByFromSequenceNumberAndToSequenceNumber(
                        fromSequenceNumber,
                        toSequenceNumber
                )
                .orElseThrow(() -> new QuizNotFoundException(fromSequenceNumber, toSequenceNumber));
        List<QuizExercise> exercises =
                quizExerciseRepository
                        .findAllByQuiz_IdOrderBySortOrderAsc(quiz.getId());

        return new QuizContent(quiz, exercises);
    }

    public void markQuizSent(Long tgUserId) {
        UserProgress progress = getUserProgress(tgUserId);
        progress.markQuizSent(progress.getLastSequenceNumber());
    }

    private UserProgress getUserProgress(Long tgUserId) {
        User user = userRepository.findByTgUserId(tgUserId)
                .orElseThrow(() -> new UserNotRegisteredException(tgUserId));

        return userProgressRepository.findById(user.getId())
                .orElseThrow(() -> new UserProgressNotFound(user.getId()));
    }
}
