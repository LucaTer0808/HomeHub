package com.terfehr.homehub.domain.household.exception;

public class InvalidVerificationCodeExpirationException extends RuntimeException {
    public InvalidVerificationCodeExpirationException(String message) {
        super(message);
    }
}
