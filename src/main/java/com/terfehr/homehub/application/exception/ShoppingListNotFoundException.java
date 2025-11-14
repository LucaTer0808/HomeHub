package com.terfehr.homehub.application.exception;

/**
 * Exception thrown when a ShoppingList cannot be found when fetching from the database.
 */
public class ShoppingListNotFoundException extends RuntimeException {
    public ShoppingListNotFoundException(String message) {
        super(message);
    }
}
