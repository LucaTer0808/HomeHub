package com.terfehr.homehub.domain.shared.exception;

public class InvalidEventPayloadException extends RuntimeException {
    public InvalidEventPayloadException(String message) {
        super(message);
    }
}
