package com.terfehr.homehub.domain.shared.exception;

public class InvalidDomainEventPayloadException extends RuntimeException {
    public InvalidDomainEventPayloadException(String message) {
        super(message);
    }
}
