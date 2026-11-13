package com.beata.wordofthedaybot.service;

import com.beata.wordofthedaybot.dto.QuizContent;
import com.beata.wordofthedaybot.entity.Quiz;
import com.beata.wordofthedaybot.entity.QuizExercise;
import com.beata.wordofthedaybot.entity.User;
import com.beata.wordofthedaybot.entity.UserProgress;
import com.beata.wordofthedaybot.exception.QuizNotFoundException;
import com.beata.wordofthedaybot.repository.QuizExerciseRepository;
import com.beata.wordofthedaybot.repository.QuizRepository;
import com.beata.wordofthedaybot.repository.UserProgressRepository;
import com.beata.wordofthedaybot.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuizMessageServiceTest {

    private static final Long TG_USER_ID = 12345678L;
    private static final Long USER_ID = 1L;

    @Mock
    private UserRepository userRepository;

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private UserProgressRepository userProgressRepository;

    @Mock
    private QuizExerciseRepository quizExerciseRepository;

    @InjectMocks
    private QuizMessageService quizMessageService;

    //isQuizDue() tests
    @Test
    void isQuizDue_shouldReturnTrue_when_SixWordsSentAndQuizNotSent() {
        //given
        mockUserProgress(6, 0);
        //when
        boolean result = quizMessageService.isQuizDue(TG_USER_ID);
        //then
        assertTrue(result);
    }

    @Test
    void isQuizDue_shouldReturnFalse_whenOnlyFiveWordsSent() {
        mockUserProgress(5, 0);
        boolean result = quizMessageService.isQuizDue(TG_USER_ID);
        assertFalse(result);
    }

    @Test
    void isQuizDue_shouldReturnFalse_whenQuizAlreadySent() {
        mockUserProgress(6, 6);
        boolean result = quizMessageService.isQuizDue(TG_USER_ID);
        assertFalse(result);
    }

    @Test
    void isQuizDue_shouldReturnFalse_whenWordCircleIsNotCompleted() {
        mockUserProgress(7, 6);
        boolean result = quizMessageService.isQuizDue(TG_USER_ID);
        assertFalse(result);
    }

    @Test
    void isQuizDue_shouldReturnTrue_whenTwelveWordsSentAndOnlyFirstQuizSent() {
        // Given
        mockUserProgress(12, 6);

        // When
        boolean result = quizMessageService.isQuizDue(TG_USER_ID);

        // Then
        assertTrue(result);
    }

    //getQuizForUser() tests
    @Test
    void getQuizForUser_shouldReturnQuizForUser() {
        User user = mock(User.class);
        UserProgress progress = mock(UserProgress.class);
        when(userRepository.findByTgUserId(TG_USER_ID))
                .thenReturn(Optional.of(user));

        when(user.getId()).thenReturn(USER_ID);

        when(userProgressRepository.findById(USER_ID))
                .thenReturn(Optional.of(progress));

        when(progress.getLastSequenceNumber()).thenReturn(6);

        Quiz quiz = mock(Quiz.class);
        QuizExercise exercise1 = mock(QuizExercise.class);
        QuizExercise exercise2 = mock(QuizExercise.class);
        List<QuizExercise> exercises = List.of(exercise1, exercise2);
        mockQuizExercise(quiz, exercises);

        QuizContent result =  quizMessageService.getQuizForUser(TG_USER_ID);

        //Then
        assertNotNull(result);
        assertSame(quiz, result.quiz());
        assertEquals(exercises, result.quizExercises());

        verify(quizRepository)
                .findByFromSequenceNumberAndToSequenceNumber(1, 6);

        verify(quizExerciseRepository)
                .findAllByQuiz_IdOrderBySortOrderAsc(10L);

    }

    @Test
    void getQuizForUser_shouldNotFindQuizForRange() {
        User user = mock(User.class);
        UserProgress progress = mock(UserProgress.class);
        when(userRepository.findByTgUserId(TG_USER_ID))
                .thenReturn(Optional.of(user));

        when(user.getId()).thenReturn(USER_ID);

        when(userProgressRepository.findById(USER_ID))
                .thenReturn(Optional.of(progress));

        when(progress.getLastSequenceNumber()).thenReturn(6);

        when(quizRepository.findByFromSequenceNumberAndToSequenceNumber(1, 6))
                .thenReturn(Optional.empty());

        //Then
    assertThrows(
            QuizNotFoundException.class,
            () -> quizMessageService.getQuizForUser(TG_USER_ID)
    );
        verify(quizRepository)
                .findByFromSequenceNumberAndToSequenceNumber(1, 6);

        verifyNoInteractions(quizExerciseRepository);
    }


    private void mockUserProgress(
            Integer lastSequenceNumber,
            Integer lastQuizSequenceNumber
    ) {
        User user = mock(User.class);
        UserProgress progress = mock(UserProgress.class);

        when(userRepository.findByTgUserId(TG_USER_ID))
                .thenReturn(Optional.of(user));

        when(user.getId()).thenReturn(USER_ID);

        when(userProgressRepository.findById(USER_ID))
                .thenReturn(Optional.of(progress));

        when(progress.getLastSequenceNumber())
                .thenReturn(lastSequenceNumber);

        when(progress.getLastQuizSequenceNumber())
                .thenReturn(lastQuizSequenceNumber);
    }

    private void mockQuizExercise(Quiz quiz, List<QuizExercise> exercises ) {

        when(quizRepository.findByFromSequenceNumberAndToSequenceNumber(1, 6))
                .thenReturn(Optional.of(quiz));

        when(quiz.getId()).thenReturn(10L);

        when(quizExerciseRepository.findAllByQuiz_IdOrderBySortOrderAsc(10L))
                .thenReturn(exercises);
    }
}
