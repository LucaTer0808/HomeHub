package com.terfehr.homehub.domain.household.exception;

public class InvalidForgotPasswordCodeExpirationException extends RuntimeException {
    public InvalidForgotPasswordCodeExpirationException(String message) {
        super(message);
    }
}
