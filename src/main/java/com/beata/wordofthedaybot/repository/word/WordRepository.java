package com.beata.wordofthedaybot.repository.word;

import com.beata.wordofthedaybot.entity.word.Word;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WordRepository extends JpaRepository<Word, Long> {

    Optional<Word> findBySequenceNumber(Integer sequenceNumber);
}
