package com.terfehr.homehub.domain.bookkeeping.exception;

public class InvalidAccountNameException extends RuntimeException {
    public InvalidAccountNameException(String message) {
        super(message);
    }
}
