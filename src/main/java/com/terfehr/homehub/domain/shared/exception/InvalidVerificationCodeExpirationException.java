package com.terfehr.homehub.domain.shared.exception;

public class InvalidVerificationCodeExpirationException extends RuntimeException {
    public InvalidVerificationCodeExpirationException(String message) {
        super(message);
    }
}
