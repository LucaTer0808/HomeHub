package com.terfehr.homehub.application.exception;

public class InvalidRefreshVerificationCodeDTOException extends RuntimeException {
    public InvalidRefreshVerificationCodeDTOException(String message) {
        super(message);
    }
}
