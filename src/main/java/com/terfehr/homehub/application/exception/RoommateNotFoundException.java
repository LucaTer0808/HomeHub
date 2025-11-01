package com.terfehr.homehub.application.exception;

public class RoommateNotFoundException extends RuntimeException {
    public RoommateNotFoundException(String message) {
        super(message);
    }
}
