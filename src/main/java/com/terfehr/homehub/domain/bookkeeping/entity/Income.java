package com.terfehr.homehub.domain.bookkeeping.entity;

import com.terfehr.homehub.domain.bookkeeping.value.Money;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class Income extends Transaction {

    private String source;

    /**
     * Constructs an Income object representing a transaction in the bookkeeping system.
     * Includes details about the amount, description, date, source, and associated account.
     * Validates the recipient to ensure data integrity.
     *
     * @param amount The monetary value of the source in the smallest currency unit (e.g., cents for USD).
     * @param description A brief description of the source.
     * @param date The date and time the source occurred.
     * @param source The source to whom the source was paid.
     * @param account The account associated with the source transaction.
     * @throws IllegalArgumentException if the source is invalid.
     */
    public Income(long amount, String description, LocalDateTime date, String source, Account account) throws IllegalArgumentException {
        super(amount, description, date, account);
        if (!validate(source)) {
            throw new IllegalArgumentException("Invalid Income object");
        }
        this.source = source;
    }

    /**
     * Sets the source of the income. It's where the money was received from.
     * @param source The source from which the income was received.
     */
    public void setSource(String source) throws IllegalArgumentException {
        if (!validateSource(source)) {
            throw new IllegalArgumentException("Invalid source");
        }
        this.source = source;
    }

    /**
     * Validates the state of the Income object. This method ensures that both the validation
     * criteria defined in the superclass Transaction and the specific validation for the
     * Income entity (i.e., the validity of the source) are satisfied.
     *
     * @return true if the Income object is valid, false otherwise.
     */
    private boolean validate(String source) {
        return validateSource(source);
    }

    /**
     * Validates the provided source string to ensure it is neither null nor empty.
     *
     * @param source The source string to be validated.
     * @return true if the source is not null and not empty, false otherwise.
     */
    private boolean validateSource(String source) {
        return source != null && !source.isEmpty();
    }
}
