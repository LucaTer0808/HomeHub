package com.terfehr.homehub.domain.shared.exception;

public class InvalidHouseholdNameException extends RuntimeException {
    public InvalidHouseholdNameException(String message) {
        super(message);
    }
}
