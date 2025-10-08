package com.terfehr.homehub.domain.bookkeeping.exception;

/**
 * Exception thrown when an account cannot be found when fetching from the database.
 */
public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String message) {
        super(message);
    }
}
