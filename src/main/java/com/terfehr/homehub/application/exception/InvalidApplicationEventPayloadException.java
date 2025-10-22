package com.terfehr.homehub.application.exception;

public class InvalidApplicationEventPayloadException extends RuntimeException {
    public InvalidApplicationEventPayloadException(String message) {
        super(message);
    }
}
