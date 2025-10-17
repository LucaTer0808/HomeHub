package com.terfehr.homehub.domain.household.exception;

public class InvalidForgotPasswordCodeException extends RuntimeException {
    public InvalidForgotPasswordCodeException(String message) {
        super(message);
    }
}
