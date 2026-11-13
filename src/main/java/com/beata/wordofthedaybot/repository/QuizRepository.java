package com.beata.wordofthedaybot.repository;

import com.beata.wordofthedaybot.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

    Optional<Quiz> findByFromSequenceNumberAndToSequenceNumber(
            Integer fromSequenceNumber,
            Integer toSequenceNumber
    );
}
