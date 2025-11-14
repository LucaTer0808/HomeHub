package com.terfehr.homehub.domain.shared.exception;

public class InvalidChangeEmailCodeExpirationException extends RuntimeException {
    public InvalidChangeEmailCodeExpirationException(String message) {
        super(message);
    }
}
