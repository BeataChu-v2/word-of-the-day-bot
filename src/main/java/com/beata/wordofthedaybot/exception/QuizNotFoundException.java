package com.beata.wordofthedaybot.exception;

public class QuizNotFoundException extends RuntimeException {
    public QuizNotFoundException(Integer fromSequenceNumber, Integer toSequenceNumber) {
        super("Quiz not found for words " + fromSequenceNumber + " to " + toSequenceNumber);
    }
}
