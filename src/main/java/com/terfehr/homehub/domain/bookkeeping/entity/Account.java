package com.terfehr.homehub.domain.bookkeeping.entity;

import com.terfehr.homehub.domain.shared.exception.*;
import com.terfehr.homehub.domain.bookkeeping.value.Money;
import com.terfehr.homehub.domain.household.entity.Household;
import com.terfehr.homehub.domain.household.entity.Roommate;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Currency;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
@Table(name = "accounts")
public class Account {

    private static final Set<String> VALID_CURRENCIES = Currency // for caching purposes
            .getAvailableCurrencies()
            .stream()
            .map(Currency::getCurrencyCode)
            .collect(Collectors.toSet());

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Money balance;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Transaction> transactions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "household_id")
    private Household household;

    /**
     * Creates a new account for the given household.
     *
     * @param name The name of the account.
     * @param amount The amount of the account.
     * @param currencyCode The currency code of the account.
     * @param household The household to which the account belongs
     * @throws InvalidAccountNameException If the provided name is invalid.
     * @throws InvalidCurrencyCodeException If the provided currency code is invalid.
     * @throws InvalidHouseholdException If the provided household is invalid.
     */
    public Account(String name, long amount, String currencyCode, Household household) throws
            InvalidAccountNameException,
            InvalidCurrencyCodeException,
            InvalidHouseholdException {
        if (!validateName(name)) {
            throw new InvalidAccountNameException("Invalid Account name given");
        }
        if (!validateCurrencyCode(currencyCode)) {
            throw new InvalidCurrencyCodeException("Invalid Currency code given");
        }
        if (!validateHousehold(household)) {
            throw new InvalidHouseholdException("Invalid Household given");
        }
        Money initialBalance = new Money(currencyCode, amount);
        this.name = name;
        this.balance = initialBalance;
        this.transactions = new HashSet<>();
        this.household = household;
    }

    /**
     * Changes the name of the account.
     *
     * @param name The new name for the account.
     * @throws InvalidAccountNameException If the provided name is invalid.
     */
    public void changeName(String name) throws InvalidAccountNameException {
        if (!validateName(name)) {
            throw new InvalidAccountNameException("Invalid Account name given");
        }
        this.name = name;
    }

    /**
     * Sets the name of the account. The name typically represents the purpose or identity
     * of the account (e.g., "Checking Account", "Savings Account").
     *
     * @param name The name to assign to the account. It must be non-null and non-empty.
     * @throws IllegalArgumentException If the account becomes invalid after setting the name.
     */
    public void setName(String name) throws IllegalArgumentException {
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
        this.balance = new Money(this.balance.getCurrency().getCurrencyCode(), balance);
    }

    /**
     * Adds an Expense to the Account by constructing an Expense object by the given parameters and updating the account balance afterward.
     * If the parameters are invalid for creating an Expense, an exception is thrown.
     *
     * @param amount The amount of the Expense in the smallest unit.
     * @param description A brief description of what was paid with this Expense.
     * @param date The timestamp of when this Expense was transferred.
     * @param recipient The recipient who received the money.
     * @param roommate The roommate who paid the money.
     * @throws IllegalArgumentException If the parameters are invalid for creating an Expense.
     */
    public void addExpense(long amount, String description, LocalDateTime date, String recipient, Roommate roommate) throws IllegalArgumentException {
        Expense expense = new Expense(amount, description, date, recipient, this, roommate);
        this.transactions.add(expense);
        updateBalance(expense);
    }

    /**
     * Adds a ShoppingExpense to the Account by constructing a ShoppingExpense object by the given parameters and updating the account balance afterward.
     * It returns the created ShoppingExpense object for setting the ShoppingSpree object later.
     *
     * @param amount The amount of the ShoppingExpense in the smallest unit.
     * @param description A brief description of what was paid with this ShoppingExpense.
     * @param date The timestamp of when this ShoppingExpense was transferred.
     * @param recipient The recipient who received the money.
     * @return The created ShoppingExpense object.
     * @throws InvalidAmountException if the amount is negative.
     * @throws InvalidDescriptionException if the description is empty.
     * @throws InvalidDateException if the date is null.
     * @throws InvalidRecipientException if the recipient is null or empty.
     * @throws InvalidAccountException if the account is null.
     * @throws InvalidRoommateException if the roommate is null.
     */
    public ShoppingExpense addShoppingExpense(long amount, String description, LocalDateTime date, String recipient, Roommate roommate) throws
            InvalidAmountException,
            InvalidDescriptionException,
            InvalidDateException,
            InvalidRecipientException,
            InvalidAccountException,
            InvalidRoommateException
    {
        ShoppingExpense expense = new ShoppingExpense(amount, description, date, recipient, this, roommate);
        this.transactions.add(expense);
        updateBalance(expense);
        return expense;
    }

    /**
     * Adds an Income to the Account by constructing an Income object by the given parameters and updating the account balance afterward.
     * If the parameters are invalid for creating an Income, an exception is thrown.
     *
     * @param amount The amount of the Income in the smallest unit.
     * @param description A brief description of what the Income is about.
     * @param date The timestamp of when this Income was transferred.
     * @param source The source who sent the money.
     * @param roommate The roommate who paid the money.
     * @throws IllegalArgumentException If the parameters are invalid for creating an Income.
     */
    public void addIncome(long amount, String description, LocalDateTime date, String source, Roommate roommate) throws IllegalArgumentException {
        Income income = new Income(amount, description, date, source, this, roommate);
        this.transactions.add(income);
        updateBalance(income);
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
    public void removeTransaction(Transaction transaction) throws IllegalArgumentException {
        if (!validateTransaction(transaction) || !transactions.contains(transaction)) {
            throw new IllegalArgumentException("Invalid or not contained Transaction object");
        }
        this.transactions.remove(transaction);
        revertBalance(transaction);
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
            balance = new Money(balance.getCurrency().getCurrencyCode(), newBalance);
        }
        else if (transaction instanceof Expense) {
            long newBalance = balance.getAmountInSmallestUnit() - transaction.getAmount().getAmountInSmallestUnit();
            balance = new Money(balance.getCurrency().getCurrencyCode(), newBalance);
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
            balance = new Money(balance.getCurrency().getCurrencyCode(), newBalance);
        }
        else if (transaction instanceof Expense) {
            long newBalance = balance.getAmountInSmallestUnit() + transaction.getAmount().getAmountInSmallestUnit();
            balance = new Money(balance.getCurrency().getCurrencyCode(), newBalance);
        }
    }

    /**
     * Validates the provided account parameters to ensure they are not null or empty.
     *
     * @param name The name to be validated.
     * @param currencyCode The currency code to be validated.
     * @param household The household to be validated.
     * @return True if all parameters are valid, false otherwise.
     */
    private boolean validate(String name, String currencyCode, Household household) {
        return validateName(name) && validateCurrencyCode(currencyCode) && validateHousehold(household);
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
     * Validates the provided currency code to ensure it is not null and a valid Currency code according to ISO 4217.
     *
     * @param currencyCode The currency code to be validated.
     * @return True if the currency code is not null and not empty, false otherwise.
     */
    private boolean validateCurrencyCode(String currencyCode) {
        return currencyCode != null && VALID_CURRENCIES.contains(currencyCode);
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
                transaction.getAccount().equals(this);
    }
}


