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
    private String description;
    private LocalDateTime date;
    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    public Transaction(Money amount, String description, LocalDateTime date, Account account) {
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.account = account;
        if (!validate()) {
            throw new IllegalArgumentException("Invalid Transaction object");
        }
    }

    /**
     * Sets the amount for the Transaction.
     * @param amount The monetary amount of the expense.
     */
    public void setAmount(Money amount) {
        if (!validateAmount(amount)) {
            throw new IllegalArgumentException("Invalid amount");
        }
        this.amount = amount;
    }

    /**
     * Sets the description for the Transaction.
     * @param description A brief description of the expense.
     */
    public void setDescription(String description) {
        if (!validateDescription(description)) {
            throw new IllegalArgumentException("Invalid description");
        }
        this.description = description;
    }

    /**
     * Sets the date for the Transaction.
     * @param date The date and time when the expense occurred.
     */
    public void setDate(LocalDateTime date) {
        if (!validateDate(date)) {
            throw new IllegalArgumentException("Invalid date");
        }
        this.date = date;
    }

    /**
     * Sets the account associated with the Transaction.
     *
     * @param account The account to be assigned to this Transaction. Must be a valid, non-null account.
     * @throws IllegalArgumentException if the provided account is invalid or null.
     */
    public void setAccount(Account account) {
        if (!validateAccount(account)) {
            throw new IllegalArgumentException("Invalid account");
        }
        this.account = account;
    }

    /**
     * Validates the state of the Transaction object. It checks if all required fields,
     * including amount, date and description, meet the defined validation criteria.
     *
     * @return true if the Transaction object is valid, false otherwise.
     */
    public boolean validate() {
        return validateAmount(amount) && validateDate(date) && validateDescription(description) && validateAccount(account);
    }

    /**
     * Validates if the provided monetary amount is not null.
     *
     * @param amount The monetary amount to be validated.
     * @return true if the amount is not null, false otherwise.
     */
    private boolean validateAmount(Money amount) {
        return amount != null;
    }

    /**
     * Validates the given date to ensure it is not null.
     *
     * @param date The date to be validated.
     * @return true if the date is not null, false otherwise.
     */
    private boolean validateDate(LocalDateTime date) {
        return date != null;
    }

    /**
     * Validates the given description to ensure it is not null or empty.
     *
     * @param description The description to be validated.
     * @return true if the description is not null and not empty, false otherwise.
     */
    private boolean validateDescription(String description) {
        return description != null && !description.isEmpty();
    }

    /**
     * Validates the given account to ensure it is not null.
     *
     * @param account The account to be validated.
     * @return true if the account is not null, false otherwise.
     */
    private boolean validateAccount(Account account) {
        return account != null;
    }
}
