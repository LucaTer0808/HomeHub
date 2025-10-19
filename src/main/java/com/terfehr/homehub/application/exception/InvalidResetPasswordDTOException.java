package com.terfehr.homehub.application.exception;

public class InvalidResetPasswordDTOException extends RuntimeException {
    public InvalidResetPasswordDTOException(String message) {
        super(message);
    }
}
