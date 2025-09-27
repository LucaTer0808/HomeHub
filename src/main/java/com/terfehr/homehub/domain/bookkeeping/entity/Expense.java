package com.terfehr.homehub.domain.bookkeeping.entity;

import com.terfehr.homehub.domain.bookkeeping.value.Money;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents an expense transaction in the bookkeeping system. An Expense object includes
 * attributes inherited from the Transaction class, such as the amount, description, and date,
 * as well as a recipient to indicate who the payment was made to. It includes validations
 * to ensure the integrity of its data.
 */
@Entity
@NoArgsConstructor
@Getter
public class Expense extends Transaction {

    private String recipient;

    /**
     * Constructs an Expense object representing a transaction in the bookkeeping system.
     * Includes details about the amount, description, date, recipient, and associated account.
     * Validates the recipient to ensure data integrity.
     *
     * @param amount The monetary value of the expense in the smallest currency unit (e.g., cents for USD).
     * @param description A brief description of the expense.
     * @param date The date and time the expense occurred.
     * @param recipient The recipient to whom the expense was paid.
     * @param account The account associated with the expense transaction.
     * @throws IllegalArgumentException if the recipient is invalid.
     */
    public Expense(long amount, String description, LocalDateTime date, String recipient, Account account) {
        super(amount, description, date, account);
        if (!validate(recipient)) {
            throw new IllegalArgumentException("Invalid Expense object");
        }
        this.recipient = recipient;
    }

    /**
     * Sets the recipient of the expense. The recipient represents
     * the entity or individual to whom the payment was made.
     *
     * @param recipient The recipient to set. Must be a non-null and non-empty string.
     * @throws IllegalArgumentException if the recipient is null or empty.
     */
    public void setRecipient(String recipient) {
        if (!validateRecipient(recipient)) {
            throw new IllegalArgumentException("Invalid recipient");
        }
        this.recipient = recipient;
    }

    /**
     * Validates the provided recipient string to ensure it adheres to specific rules.
     *
     * @param recipient The recipient string to be validated.
     * @return true if the recipient passes validation criteria, false otherwise.
     */
    private boolean validate(String recipient) {
        return validateRecipient(recipient);
    }

    /**
     * Validates the provided recipient string to ensure it is neither null nor empty.
     *
     * @param recipient The recipient string to be validated.
     * @return true if the recipient is not null and not empty, false otherwise.
     */
    private boolean validateRecipient(String recipient) {
        return recipient != null && !recipient.isEmpty();
    }
}
