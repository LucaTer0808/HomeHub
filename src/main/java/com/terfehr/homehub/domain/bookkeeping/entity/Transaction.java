package com.terfehr.homehub.domain.bookkeeping.entity;

import com.terfehr.homehub.domain.bookkeeping.value.Money;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Transaction entity representing an expense record in the bookkeeping system.
 * Each Transaction belongs to a specific Account and has attributes such as amount, date and description.
 */
@Entity
@NoArgsConstructor
@Getter
public abstract class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Money amount;
    String description;
    LocalDateTime date;

    public Transaction(Money amount, String description, LocalDateTime date) {
        this.amount = amount;
        this.description = description;
        this.date = date;
        if (!validate()) {
            throw new IllegalArgumentException("Invalid Transaction object");
        }
    }

    /**
     * Sets the amount for the Transaction.
     * @param amount The monetary amount of the expense.
     */
    public void setAmount(Money amount) {
        this.amount = amount;
        if (!validate()) {
            throw new IllegalArgumentException("Invalid Transaction object");
        }
    }

    /**
     * Sets the description for the Transaction.
     * @param description A brief description of the expense.
     */
    public void setDescription(String description) {
        this.description = description;
        if (!validate()) {
            throw new IllegalArgumentException("Invalid Transaction object");
        }
    }

    /**
     * Sets the date for the Transaction.
     * @param date The date and time when the expense occurred.
     */
    public void setDate(LocalDateTime date) {
        this.date = date;
        if (!validate()) {
            throw new IllegalArgumentException("Invalid Transaction object");
        }
    }

    /**
     * Validates the Transaction object to ensure all required fields are set and valid.
     * @return True if the Transaction object is valid, false otherwise.
     */
    public boolean validate() {
        return amount != null && amount.validate() && date != null && description != null && !description.isEmpty();
    }
}
