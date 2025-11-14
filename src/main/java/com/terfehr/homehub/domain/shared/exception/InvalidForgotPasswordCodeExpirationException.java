package com.terfehr.homehub.domain.shared.exception;

public class InvalidForgotPasswordCodeExpirationException extends RuntimeException {
    public InvalidForgotPasswordCodeExpirationException(String message) {
        super(message);
    }
}
