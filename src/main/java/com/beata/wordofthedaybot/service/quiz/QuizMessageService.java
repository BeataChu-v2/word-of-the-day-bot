package com.beata.wordofthedaybot.service.quiz;

import com.beata.wordofthedaybot.dto.QuizContent;
import com.beata.wordofthedaybot.entity.quiz.Quiz;
import com.beata.wordofthedaybot.entity.quiz.QuizExercise;
import com.beata.wordofthedaybot.entity.user.User;
import com.beata.wordofthedaybot.entity.user.UserProgress;
import com.beata.wordofthedaybot.exception.QuizNotFoundException;
import com.beata.wordofthedaybot.exception.UserNotRegisteredException;
import com.beata.wordofthedaybot.exception.UserProgressNotFound;
import com.beata.wordofthedaybot.repository.quiz.QuizExerciseRepository;
import com.beata.wordofthedaybot.repository.quiz.QuizRepository;
import com.beata.wordofthedaybot.repository.user.UserProgressRepository;
import com.beata.wordofthedaybot.repository.user.UserRepository;
import com.beata.wordofthedaybot.service.word.WordContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizMessageService {
    private final UserRepository userRepository;
    private final QuizRepository quizRepository;
    private final UserProgressRepository userProgressRepository;
    private final QuizExerciseRepository quizExerciseRepository;
    private final WordContentService wordContentService;

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

        List<String> coveredWords = new ArrayList<>();

        for (int i = fromSequenceNumber; i <= toSequenceNumber; i++) {
            coveredWords.add(wordContentService.getWordBySequenceNumber(i));
        }

        return new QuizContent(quiz, exercises, coveredWords);
    }

    @Transactional
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
