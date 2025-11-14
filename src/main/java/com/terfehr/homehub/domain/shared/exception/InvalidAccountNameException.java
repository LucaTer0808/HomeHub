package com.terfehr.homehub.domain.shared.exception;

public class InvalidAccountNameException extends RuntimeException {
    public InvalidAccountNameException(String message) {
        super(message);
    }
}
