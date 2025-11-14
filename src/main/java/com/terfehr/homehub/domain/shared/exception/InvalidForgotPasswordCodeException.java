package com.terfehr.homehub.domain.shared.exception;

public class InvalidForgotPasswordCodeException extends RuntimeException {
    public InvalidForgotPasswordCodeException(String message) {
        super(message);
    }
}
