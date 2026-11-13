package com.beata.wordofthedaybot.dto;

import com.beata.wordofthedaybot.entity.Quiz;
import com.beata.wordofthedaybot.entity.QuizExercise;

import java.util.List;

public record QuizContent(
        Quiz quiz,
        List<QuizExercise> quizExercises
) {}
