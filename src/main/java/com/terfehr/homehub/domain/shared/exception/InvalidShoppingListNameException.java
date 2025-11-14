package com.terfehr.homehub.domain.shared.exception;

public class InvalidShoppingListNameException extends RuntimeException {
    public InvalidShoppingListNameException(String message) {
        super(message);
    }
}
