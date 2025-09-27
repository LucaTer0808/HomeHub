package com.terfehr.homehub.domain.bookkeeping.entity;

import com.terfehr.homehub.domain.bookkeeping.value.Money;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;

/**
 * Represents a financial account in the bookkeeping system. An Account can hold multiple Transaction and
 * Income records, allowing users to track their financial activities across different accounts such as checking,
 * savings, or credit cards. <strong>Aggregate</strong> to {@link Transaction} and {@link Income}.
 *
 * TODO: Implement Household
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
    private HashSet<Transaction> transactions;
    private Household household;

    /**
     * Constructs a new instance of Account with the specified name, initial balance,
     * and associated household. Ensures the provided parameters are valid.
     *
     * @param name The name of the account, typically representing what the account
     *             is used for (e.g., "Checking Account").
     * @param initialBalance The initial monetary balance of the account.
     * @param household The household associated with the account, representing ownership.
     * @throws IllegalArgumentException If any of the provided parameters are invalid.
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
     * Updates the balance of the account. The balance represents the current monetary
     * amount available in the account. This method ensures the account remains valid
     * after updating the balance.
     *
     * @param balance The new monetary balance to set for the account. It must be an
     *                instance of {@link Money} and pass its own validation checks.
     * @throws IllegalArgumentException If the account becomes invalid after setting the balance.
     */
    public void setBalance(Money balance) {
        if (!validateBalance(balance)) {
            throw new IllegalArgumentException("Invalid Account object");
        }
        this.balance = balance;
    }

    /**
     * Sets the household associated with this account. The household represents
     * the group or entity that owns the account. This method ensures the account
     * remains valid after updating the household.
     *
     * @param household The household to associate with this account. It must be
     *                  non-null and pass its own validation checks.
     * @throws IllegalArgumentException If the account becomes invalid after setting the household.
     */
    public void setHousehold(Household household) {
        if (!validateHousehold(household)) {
            throw new IllegalArgumentException("Invalid Account object");
        }
        this.household = household;
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
     * Validates the Account object to ensure all required fields are properly set
     * and meet the conditions for a valid account. The validation checks that:
     * - The account's name is not null or empty.
     * - The balance is not null and passes its own validation.
     * - The household associated with the account is not null.
     *
     * @return True if the Account object is valid based on the specified criteria, false otherwise.
     */
    public boolean validate(String name, Money balance, Household household) {
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
            balance = new Money(balance.getCurrencyCode(), newBalance, balance.getScale());
        }
        if (transaction instanceof Expense) {
            long newBalance = balance.getAmountInSmallestUnit() - transaction.getAmount().getAmountInSmallestUnit();
            balance = new Money(balance.getCurrencyCode(), newBalance, balance.getScale());
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
            balance = new Money(balance.getCurrencyCode(), newBalance, balance.getScale());
        }
        if (transaction instanceof Expense) {
            long newBalance = balance.getAmountInSmallestUnit() + transaction.getAmount().getAmountInSmallestUnit();
            balance = new Money(balance.getCurrencyCode(), newBalance, balance.getScale());
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
     * Validates the specified balance to ensure it is not null and complies with its own validation rules.
     *
     * @param balance The monetary balance to validate. Must not be null and must pass its own validation checks.
     * @return True if the balance is valid, false otherwise.
     */
    private boolean validateBalance(Money balance) {
        return balance != null && balance.validate();
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
        return household != null && household.validate();
    }

    /**
     * Validates the given transaction to ensure it is valid.
     * A transaction is considered valid if it is not null and passes its own validation.
     *
     * @param transaction The transaction object to be validated. Must not be null and should
     *                    pass its internal validation checks.
     * @return True if the transaction is valid, false otherwise.
     */
    private boolean validateTransaction(Transaction transaction) {
        return transaction != null && transaction.validate();
    }
}


