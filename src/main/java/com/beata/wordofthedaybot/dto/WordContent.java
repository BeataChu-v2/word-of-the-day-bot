package com.beata.wordofthedaybot.dto;

import com.beata.wordofthedaybot.entity.word.Collocation;
import com.beata.wordofthedaybot.entity.word.Phrase;
import com.beata.wordofthedaybot.entity.word.Word;

import java.util.List;

public record WordContent(
        Word word,
        List<Collocation> collocations,
        List<Phrase> phrases
) {
}
