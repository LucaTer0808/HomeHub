package com.terfehr.homehub.domain.shared.exception;

public class InvalidRecipientException extends RuntimeException {
    public InvalidRecipientException(String message) {
        super(message);
    }
}
