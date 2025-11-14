package com.terfehr.homehub.domain.shared.exception;

public class InvalidShoppingListException extends RuntimeException {
    public InvalidShoppingListException(String message) {
        super(message);
    }
}
