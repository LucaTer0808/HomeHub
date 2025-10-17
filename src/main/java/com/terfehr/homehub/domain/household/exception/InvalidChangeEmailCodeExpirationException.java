package com.terfehr.homehub.domain.household.exception;

public class InvalidChangeEmailCodeExpirationException extends RuntimeException {
    public InvalidChangeEmailCodeExpirationException(String message) {
        super(message);
    }
}
