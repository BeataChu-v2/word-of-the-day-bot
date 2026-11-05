package com.beata.wordofthedaybot.repository;

import com.beata.wordofthedaybot.entity.Phrase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhraseRepository
        extends JpaRepository<Phrase, Long> {
    List<Phrase> findAllByWord_IdOrderBySortOrderAsc(Long wordId);
}
