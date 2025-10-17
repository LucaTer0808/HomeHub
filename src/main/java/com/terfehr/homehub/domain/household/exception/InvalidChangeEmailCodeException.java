package com.terfehr.homehub.domain.household.exception;

public class InvalidChangeEmailCodeException extends RuntimeException {
    public InvalidChangeEmailCodeException(String message) {
        super(message);
    }
}
