package com.beata.wordofthedaybot.exception;

public class MessageLengthExceededException extends RuntimeException {
    public MessageLengthExceededException() {

        super("Message length exceeded 4096 symbols");
    }
}
