package com.beata.wordofthedaybot.exception;

public class UserNotRegisteredException extends RuntimeException {
    public UserNotRegisteredException() {
        super("User is not registered");
    }
}
