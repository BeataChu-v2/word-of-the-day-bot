package com.beata.wordofthedaybot.service;

import com.beata.wordofthedaybot.dto.WordContent;
import com.beata.wordofthedaybot.service.word.WordContentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class WordContentServiceIntegrationTest {

    @Autowired
    private WordContentService wordService;

    @Test
    void shouldLoadWordWithAllItsContent() {
        WordContent content = wordService
                .findContentBySequenceNumber(1)
                .orElseThrow();

        assertEquals("to look", content.word().getWord());
        assertEquals("смотреть; выглядеть", content.word().getTranslation());
        assertNotNull(content.word().getImageFileId());

        assertEquals(4, content.collocations().size());
        assertEquals(5, content.phrases().size());

        assertEquals("look at", content.collocations().getFirst().getText());

        assertEquals("This street looks familiar", content.phrases().getFirst().getText());

    }
}
