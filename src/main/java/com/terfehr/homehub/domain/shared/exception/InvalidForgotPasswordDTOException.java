package com.terfehr.homehub.domain.shared.exception;

public class InvalidForgotPasswordDTOException extends RuntimeException {
    public InvalidForgotPasswordDTOException(String message) {
        super(message);
    }
}
