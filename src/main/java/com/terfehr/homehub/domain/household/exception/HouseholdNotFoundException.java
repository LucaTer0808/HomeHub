package com.terfehr.homehub.domain.household.exception;

/**
 * Exception thrown when a Household cannot be found when fetching from the database.
 */
public class HouseholdNotFoundException extends RuntimeException {
    public HouseholdNotFoundException(String message) {
        super(message);
    }
}
