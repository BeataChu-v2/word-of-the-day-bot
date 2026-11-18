package com.beata.wordofthedaybot.service;

import com.beata.wordofthedaybot.dto.QuizContent;
import com.beata.wordofthedaybot.dto.WordContent;
import com.beata.wordofthedaybot.entity.quiz.QuizExercise;
import com.beata.wordofthedaybot.entity.word.Collocation;
import com.beata.wordofthedaybot.entity.word.Phrase;
import com.beata.wordofthedaybot.entity.word.Word;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TelegramHtmlFormatter {
    public String formatQuizMessage(QuizContent quizContent) {
        StringBuilder text = new StringBuilder();
        text.append("#wordoftheday #quiz ")
                .append(quizContent.quiz().getTitle())
                .append("\n\n")
                .append("WORDS: <b>");
        quizContent.coveredWords().forEach(word ->
                text.append(word.toUpperCase()).append(" • "));

        text.append("</b>\n\n");
        List<QuizExercise> exercises = quizContent.quizExercises();
        for (int i = 0; i < exercises.size(); i++) {
            text.append("<b>Exercise ").append(i + 1).append(". ")
                    .append(exercises.get(i).getInstruction()).append("</b>\n")
                    .append(exercises.get(i).getContent())
                    .append("\n\n");
        };
        return text.toString();
    }

    public String formatWordMessage(WordContent content) {
        StringBuilder text = new StringBuilder();
        Word word = content.word();
        text.append("#wordoftheday ")
                .append(word.getSequenceNumber())
                .append("\n\n");

                text.append("Word: <b>")
                .append(escapeHtml(word.getWord().toUpperCase()))
                .append("</b> — ")
                .append(escapeHtml(word.getTranslation()))
                .append("\n\n");

        text.append("<b>Collocations:</b>\n")
                        .append(formatCollocations(content.collocations()));

        text.append("\n<b>Phrases:</b>\n")
                .append(formatPhrases(content.phrases()));

        return text.toString();
    }

    private String escapeHtml(String text) {
        if (text == null) {
            return "";
        }
        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }

    private String formatCollocations(List<Collocation> collocations) {
        StringBuilder result = new StringBuilder();

        collocations.forEach(collocation -> {
            result.append("• <i>")
                    .append(escapeHtml(collocation.getText()))
                    .append("</i> — ")
                    .append(escapeHtml(collocation.getTranslation()))
                    .append("\n");
        });

        return result.toString();
    }

    private String formatPhrases(List<Phrase> phrases) {
        StringBuilder result = new StringBuilder();

        phrases.forEach(phrase -> {
            result.append("• <i>")
                    .append(escapeHtml(phrase.getText()))
                    .append("</i> — ")
                    .append(escapeHtml(phrase.getTranslation()))
                    .append("\n");
        });

        return result.toString();
    }
}
