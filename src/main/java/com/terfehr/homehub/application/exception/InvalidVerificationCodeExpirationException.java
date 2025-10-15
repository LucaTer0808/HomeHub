package com.terfehr.homehub.application.exception;

public class InvalidVerificationCodeExpirationException extends RuntimeException {
    public InvalidVerificationCodeExpirationException(String message) {
        super(message);
    }
}
