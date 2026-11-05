package com.beata.wordofthedaybot.exception;

public class NoMoreWordsException extends RuntimeException {
    public NoMoreWordsException() {

        super("No more words available");
    }
}
