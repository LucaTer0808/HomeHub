package com.terfehr.homehub.domain.household.exception;

public class InvalidVerifyEmailChangeDTO extends RuntimeException {
    public InvalidVerifyEmailChangeDTO(String message) {
        super(message);
    }
}
