package com.terfehr.homehub.domain.bookkeeping.entity;

import com.terfehr.homehub.domain.bookkeeping.value.Money;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents a financial transaction in a bookkeeping system.
 * This class serves as an abstract base class for specific types of transactions,
 * such as expenses or income. It includes attributes for the monetary amount,
 * description, date, and the associated account. Validation logic ensures the integrity of the data.
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

    /**
     * Creates a new Transaction object with the specified amount, description, date, and account.
     * Ensures that all provided parameters are valid before initializing the Transaction object.
     *
     * @param amount The monetary amount for the transaction, represented in the smallest currency unit (e.g., cents for USD).
     * @param description A brief description or note detailing the transaction.
     * @param date The date and time of the transaction.
     * @param account The account associated with the transaction.
     * @throws IllegalArgumentException if any of the provided parameters are invalid.
     */
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
     * @throws IllegalArgumentException if the amount is not valid.
     */
    public void setAmount(long amount) {
        if (!validateAmount(amount)) {
            throw new IllegalArgumentException("Invalid amount");
        }
        this.amount = new Money(account.getBalance().getCurrency(), amount);
    }

    /**
     * Sets the description for the Transaction.
     * The description must not be null or empty.
     *
     * @param description The description of the Transaction.
     * @throws IllegalArgumentException if the description is not valid.
     */
    public void setDescription(String description) {
        if (!validateDescription(description)) {
            throw new IllegalArgumentException("Invalid description");
        }
        this.description = description;
    }

    /**
     * Sets the date for the Transaction.
     * The date must be valid and not null.
     *
     * @param date The date and time of the Transaction to be set.
     * @throws IllegalArgumentException if the given date is invalid.
     */
    public void setDate(LocalDateTime date) {
        if (!validateDate(date)) {
            throw new IllegalArgumentException("Invalid date");
        }
        this.date = date;
    }

    /**
     * Validates the properties of a transaction to ensure they meet the required criteria.
     *
     * @param amount The monetary amount for the transaction, represented in the smallest currency unit (e.g., cents for USD).
     * @param description A brief description or note detailing the transaction.
     * @param date The date and time of the transaction.
     * @param account The account associated with the transaction.
     * @return true if all provided parameters are valid, false otherwise.
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
     * Validates the given monetary amount to ensure it meets the required criteria.
     *
     * @param amount The monetary amount to be validated, represented in the smallest currency unit (e.g., cents for USD).
     * @return true if the amount is greater than zero, false otherwise.
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
