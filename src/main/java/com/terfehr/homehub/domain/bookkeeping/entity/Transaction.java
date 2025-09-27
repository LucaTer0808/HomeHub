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

    public Transaction(long amount, String description, LocalDateTime date, Account account) {
        if (!this.validateTransaction(amount, description, date, account)) {
            throw new IllegalArgumentException("Invalid Transaction object");
        }
        this.amount = new Money(account.getBalance().getCurrency(), amount); // default to account currency
        this.description = description;
        this.date = date;
        this.account = account;
    }

    /**
     * Sets the monetary amount for the Transaction.
     * The amount must be greater than zero and will be associated with the account's currency.
     *
     * @param amount The monetary amount to be set, represented in the smallest currency unit (e.g., cents for USD).
     *               Throws IllegalArgumentException if the amount is not valid.
     */
    public void setAmount(long amount) {
        if (!validateAmount(amount)) {
            throw new IllegalArgumentException("Invalid amount");
        }
        this.amount = new Money(account.getBalance().getCurrency(), amount);
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
     * Validates the state of the Transaction object. It checks if all required fields,
     * including amount, date and description, meet the defined validation criteria.
     *
     * @return true if the Transaction object is valid, false otherwise.
     */
    private boolean validateTransaction(long amount, String description, LocalDateTime date, Account account) {
        return validateDate(date) && validateDescription(description) && validateAmount(amount) && validateAccount(account);
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
     * Validates the given monetary amount to ensure it is greater than zero.
     *
     * @param amount The monetary amount to be validated, represented as a Money object.
     * @return true if the monetary amount is valid (greater than zero), false otherwise.
     */
    private boolean validateAmount(long amount) {
        return amount > 0;
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
