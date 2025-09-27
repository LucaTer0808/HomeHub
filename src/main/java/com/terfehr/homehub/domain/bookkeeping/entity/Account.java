package com.terfehr.homehub.domain.bookkeeping.entity;

import com.terfehr.homehub.domain.bookkeeping.value.Money;
import com.terfehr.homehub.domain.household.Household;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a financial account that belongs to a household and manages a set
 * of transactions. Each account has a name, a balance, and a collection of
 * transactions that affect the balance. Accounts can belong to a specific
 * household and support operations for adding, removing, and managing
 * transactions. The account works as the aggregate root for its transactions.
 *
 * This class provides validation mechanisms to ensure the consistency of the
 * account data when modifying its properties or performing operations such as
 * adding or removing transactions.
 */
@Entity
@NoArgsConstructor
@Getter
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Money balance;
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Transaction> transactions;
    @ManyToOne(fetch = FetchType.LAZY)
    private Household household;

    /**
     * Constructs a new Account object with the specified name, initial balance, and household.
     * The account will have an empty set of transactions upon creation.
     *
     * @param name          The name of the account, which typically identifies its purpose
     *                       (e.g., "Checking Account", "Savings Account"). Must not be null or empty.
     * @param initialBalance The initial monetary balance for the account. Must not be null.
     * @param household      The household associated with this account, representing the group or entity
     *                       that owns the account. Must not be null.
     */
    public Account(String name, Money initialBalance, Household household) {
        if (!validate(name, initialBalance, household)) {
            throw new IllegalArgumentException("Invalid Account object");
        }
        this.name = name;
        this.balance = initialBalance;
        this.transactions = new HashSet<>();
        this.household = household;
    }

    /**
     * Sets the name of the account. The name typically represents the purpose or identity
     * of the account (e.g., "Checking Account", "Savings Account").
     *
     * @param name The name to assign to the account. It must be non-null and non-empty.
     * @throws IllegalArgumentException If the account becomes invalid after setting the name.
     */
    public void setName(String name) {
        if (!validateName(name)) {
            throw new IllegalArgumentException("Invalid Account object");
        }
        this.name = name;
    }

    /**
     * Updates the account balance to the specified value while maintaining the currency of the
     * existing balance.
     *
     * @param balance The new balance amount in the smallest monetary unit (e.g., cents for USD).
     *                Must be a valid long value representing the updated financial balance.
     */
    public void setBalance(long balance) {
        this.balance = new Money(this.balance.getCurrency(), balance);
    }

    /**
     * Adds a transaction to the account. This method validates the transaction
     * to ensure it is not invalid and no duplicates exist within the account's transactions.
     * If valid, the transaction is added to the account and the account's balance is updated accordingly.
     *
     * @param transaction The transaction to be added to the account. It must be valid
     *                    and not already contained within the account's existing transactions.
     * @throws IllegalArgumentException If the provided transaction is invalid or already contained in the account.
     */
    public void addTransaction(Transaction transaction) {
        if (!validateTransaction(transaction) || transactions.contains(transaction)) {
            throw new IllegalArgumentException("Invalid or already contained Transaction object");
        }
        this.transactions.add(transaction);
        updateBalance(transaction);
    }

    /**
     * Removes the specified transaction from the account. This operation will validate
     * the transaction to ensure it is associated with the account and is valid. Upon
     * removal, the account's balance will be updated to reflect the reversal of the transaction.
     *
     * @param transaction The transaction to be removed from the account. It must be both
     *                    valid and already contained within the account.
     * @throws IllegalArgumentException If the provided transaction is invalid or not
     *                                  associated with the account.
     */
    public void removeTransaction(Transaction transaction) {
        if (!validateTransaction(transaction) || !transactions.contains(transaction)) {
            throw new IllegalArgumentException("Invalid or not contained Transaction object");
        }
        this.transactions.remove(transaction);
        revertBalance(transaction);
    }

    /**
     * Validates the provided parameters to ensure they meet the required conditions
     * for creating or updating an Account object.
     *
     * @param name The name of the account to validate. Must not be null or empty.
     * @param balance The monetary balance to validate. Must not be null.
     * @param household The household associated with the account. Must not be null.
     * @return True if all the parameters are valid, false otherwise.
     */
    private boolean validate(String name, Money balance, Household household) {
        return validateName(name) && validateBalance(balance) && validateHousehold(household);
    }

    /**
     * Updates the balance of the account based on the provided transaction. If the transaction
     * is of type Income, the amount is added to the current balance. If the transaction is of
     * type Expense, the amount is subtracted from the current balance.
     *
     * @param transaction The transaction to process for updating the account balance. Must be an instance of
     *                    either Income or Expense and contain a valid monetary amount.
     */
    private void updateBalance(Transaction transaction) {
        if (transaction instanceof Income) {
            long newBalance = balance.getAmountInSmallestUnit() + transaction.getAmount().getAmountInSmallestUnit();
            balance = new Money(balance.getCurrency(), newBalance);
        }
        else if (transaction instanceof Expense) {
            long newBalance = balance.getAmountInSmallestUnit() - transaction.getAmount().getAmountInSmallestUnit();
            balance = new Money(balance.getCurrency(), newBalance);
        }
    }

    /**
     * Reverts the account balance based on the specified transaction. If the transaction
     * is an instance of Income, the transaction amount is subtracted from the current balance.
     * If the transaction is an instance of Expense, the transaction amount is added back to the
     * current balance.
     *
     * @param transaction The transaction based on which the balance should be reverted. Must be a valid
     *                    instance of either Income or Expense, containing a monetary amount to adjust
     *                    the balance accordingly.
     */
    private void revertBalance(Transaction transaction) {
        if (transaction instanceof Income) {
            long newBalance = balance.getAmountInSmallestUnit() - transaction.getAmount().getAmountInSmallestUnit();
            balance = new Money(balance.getCurrency(), newBalance);
        }
        else if (transaction instanceof Expense) {
            long newBalance = balance.getAmountInSmallestUnit() + transaction.getAmount().getAmountInSmallestUnit();
            balance = new Money(balance.getCurrency(), newBalance);
        }
    }

    /**
     * Validates the provided name to ensure it is not null or empty.
     *
     * @param name The name to be validated.
     * @return True if the name is not null and not empty, false otherwise.
     */
    private boolean validateName(String name) {
        return name != null && !name.isEmpty();
    }

    /**
     * Validates the specified balance to ensure it is not null.
     *
     * @param balance The monetary balance to validate. Must not be null and must pass its own validation checks.
     * @return True if the balance is valid, false otherwise.
     */
    private boolean validateBalance(Money balance) {
        return balance != null;
    }

    /**
     * Validates the given household object to ensure it is not null and passes
     * its own internal validation.
     *
     * @param household The household object to validate. Must not be null and should
     *                  pass its internal validation checks.
     * @return True if the household is valid, false otherwise.
     */
    private boolean validateHousehold(Household household) {
        return household != null;
    }

    /**
     * Validates the provided transaction to ensure it is not null and is associated with the current account.
     *
     * @param transaction The transaction to be validated. Must not be null and must have an associated account
     *                    that matches the current account.
     * @return True if the transaction is valid, false otherwise.
     */
    private boolean validateTransaction(Transaction transaction) {
        return transaction != null &&
                transaction.getAccount() == this;
    }
}


