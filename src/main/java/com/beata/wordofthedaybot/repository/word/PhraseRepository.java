package com.beata.wordofthedaybot.repository.word;

import com.beata.wordofthedaybot.entity.word.Phrase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhraseRepository
        extends JpaRepository<Phrase, Long> {
    List<Phrase> findAllByWord_IdOrderBySortOrderAsc(Long wordId);
}
