package com.beata.wordofthedaybot.repository;

import com.beata.wordofthedaybot.entity.QuizExercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizExerciseRepository extends JpaRepository<QuizExercise, Long> {

    List<QuizExercise> findAllByQuiz_IdOrderBySortOrderAsc(Long quizId);
}
