package com.beata.wordofthedaybot.repository;

import com.beata.wordofthedaybot.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WordRepository extends JpaRepository<Word, Long> {

    Optional<Word> findBySequenceNumber(Integer sequenceNumber);
}
