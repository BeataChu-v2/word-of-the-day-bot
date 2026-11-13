package com.beata.wordofthedaybot.exception;

public class UserProgressNotFound extends RuntimeException {
    public UserProgressNotFound(Long userId) {
        super("User progress not found for user id " + userId);
    }
}
