package com.beata.wordofthedaybot.service;

import com.beata.wordofthedaybot.dto.WordContent;
import com.beata.wordofthedaybot.repository.CollocationRepository;
import com.beata.wordofthedaybot.repository.ImageRepository;
import com.beata.wordofthedaybot.repository.PhraseRepository;
import com.beata.wordofthedaybot.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WordContentService {

    private final WordRepository wordRepository;
    private final CollocationRepository collocationRepository;
    private final PhraseRepository phraseRepository;
    private final ImageRepository imageRepository;

    public Optional<WordContent> findContentBySequenceNumber(Integer sequenceNumber) {
        if (sequenceNumber == null || sequenceNumber < 1) {
            throw new IllegalArgumentException("Sequence number must be greater than zero");
        }
        return wordRepository.findBySequenceNumber(sequenceNumber)
                .map(word -> new WordContent(
                        word,
                        collocationRepository.findAllByWord_IdOrderBySortOrderAsc(word.getId()),
                        phraseRepository.findAllByWord_IdOrderBySortOrderAsc(word.getId()),
                        imageRepository.findAllByWord_IdOrderBySortOrderAsc(word.getId())
                ));
    }
}
