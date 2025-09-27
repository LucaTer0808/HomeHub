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
     * Constructs an Expense object.
     *
     * @param amount The monetary amount of the expense, represented as a Money object.
     * @param description A brief description of the expense transaction.
     * @param date The date and time when the expense occurred.
     * @param recipient The recipient to whom the expense was paid.
     * @throws IllegalArgumentException if the Expense object is invalid based on validation rules.
     */
    public Expense(long amount, String description, LocalDateTime date, String recipient, Account account) {
        super(amount, description, date, account);
        if (!validate(recipient)) {
            throw new IllegalArgumentException("Invalid Expense object");
        }
        this.recipient = recipient;
    }

    /**
     * Sets the recipient of the expense. It's who the money was paid to.
     * @param recipient The recipient to whom the expense was paid.
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
    public boolean validate(String recipient) {
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
