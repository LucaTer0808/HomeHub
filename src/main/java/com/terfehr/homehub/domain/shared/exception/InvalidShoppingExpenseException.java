package com.terfehr.homehub.domain.shared.exception;

public class InvalidShoppingExpenseException extends RuntimeException {
    public InvalidShoppingExpenseException(String message) {
        super(message);
    }
}
