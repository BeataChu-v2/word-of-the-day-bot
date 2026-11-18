package com.beata.wordofthedaybot.dto;

import com.beata.wordofthedaybot.entity.quiz.Quiz;
import com.beata.wordofthedaybot.entity.quiz.QuizExercise;

import java.util.List;

public record QuizContent(
        Quiz quiz,
        List<QuizExercise> quizExercises,
        List<String> coveredWords
) {

}
