package com.terfehr.homehub.domain.shared.exception;

public class InvalidChangeEmailCodeException extends RuntimeException {
    public InvalidChangeEmailCodeException(String message) {
        super(message);
    }
}
