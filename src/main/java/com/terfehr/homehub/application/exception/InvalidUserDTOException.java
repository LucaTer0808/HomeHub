package com.terfehr.homehub.application.exception;

public class InvalidUserDTOException extends RuntimeException {
    public InvalidUserDTOException(String message) {
        super(message);
    }
}
