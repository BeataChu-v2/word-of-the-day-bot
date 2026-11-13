package com.beata.wordofthedaybot.exception;

public class UserNotRegisteredException extends RuntimeException {
    public UserNotRegisteredException(Long tgUserId) {
        super("User is not registered for telegram user id " + tgUserId);
    }
}
