package com.beata.wordofthedaybot.dto;

import com.beata.wordofthedaybot.entity.Collocation;
import com.beata.wordofthedaybot.entity.Image;
import com.beata.wordofthedaybot.entity.Phrase;
import com.beata.wordofthedaybot.entity.Word;

import java.util.List;

public record WordContent(
        Word word,
        List<Collocation> collocations,
        List<Phrase> phrases,
        List<Image> images
) {
}
