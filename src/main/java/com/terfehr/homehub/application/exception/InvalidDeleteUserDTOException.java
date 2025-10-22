package com.terfehr.homehub.application.exception;

public class InvalidDeleteUserDTOException extends RuntimeException {
    public InvalidDeleteUserDTOException(String message) {
        super(message);
    }
}
