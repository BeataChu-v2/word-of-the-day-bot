package com.beata.wordofthedaybot.exception;

import java.util.function.Supplier;

public class QuizNotFoundException extends RuntimeException {
    public QuizNotFoundException(Integer fromSequenceNumber, Integer toSequenceNumber) {
        super("Quiz not found for words " + fromSequenceNumber + " to " + toSequenceNumber);
    }
}
