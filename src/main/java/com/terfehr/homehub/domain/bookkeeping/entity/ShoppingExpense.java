package com.terfehr.homehub.domain.bookkeeping.entity;

import com.terfehr.homehub.domain.household.entity.Roommate;
import com.terfehr.homehub.domain.shared.exception.*;
import com.terfehr.homehub.domain.shopping.entity.ShoppingSpree;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "shopping_expenses")
public class ShoppingExpense extends Expense {

    @OneToOne(mappedBy = "shoppingExpense")
    @JoinColumn(name = "shopping_spree_id", nullable = false)
    private ShoppingSpree shoppingSpree;

    /**
     * Constructs a ShoppingExpense object representing a transaction in the bookkeeping system.
     *
     * @param amount The monetary value of the expense in the smallest currency unit (e.g., cents for USD).
     * @param description A brief description of the expense.
     * @param date The date and time the expense occurred.
     * @param recipient The recipient to whom the expense was paid.
     * @param account The account associated with the expense transaction.
     * @param roommate The roommate associated with the expense transaction.
     * @throws InvalidAmountException if the amount is negative
     * @throws InvalidDescriptionException if the description is empty
     * @throws InvalidDateException if the date is null
     * @throws InvalidRecipientException if the recipient is null or empty
     * @throws InvalidAccountException if the account is null
     * @throws InvalidRoommateException if the roommate is null
     * @throws InvalidShoppingSpreeException if the given ShoppingSpree is invalid.
     */
    public ShoppingExpense(long amount, String description, LocalDateTime date, String recipient, Account account, Roommate roommate) throws InvalidAmountException,
        InvalidDescriptionException,
        InvalidDateException,
        InvalidRecipientException,
        InvalidAccountException,
        InvalidRoommateException
    {
        super(amount, description, date, recipient, account, roommate);
        this.shoppingSpree = null;
    }

    /**
     * Removes the ShoppingSpree associated with this ShoppingExpense.
     */
    public void removeShoppingSpree() {
        this.shoppingSpree = null;
    }

    /**
     * Sets the ShoppingSpree associated with this ShoppingExpense.
     *
     * @param shoppingSpree The ShoppingSpree to set.
     */
    public void setShoppingSpree(ShoppingSpree shoppingSpree) {
        if (!validateShoppingSpree(shoppingSpree)) {
            throw new InvalidShoppingSpreeException("Invalid ShoppingSpree object");
        }
        this.shoppingSpree = shoppingSpree;
    }

    /**
     * Validates the given ShoppingSpree object. It has to be not null and have the same ShoppingExpense as this object.
     *
     * @param shoppingSpree The ShoppingSpree to validate.
     * @return True, if the ShoppingSpree is valid. False otherwise.
     */
    private boolean validateShoppingSpree(ShoppingSpree shoppingSpree) {
        return shoppingSpree != null;
    }
}
