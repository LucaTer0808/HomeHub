package com.terfehr.homehub.domain.bookkeeping.entity;

import com.terfehr.homehub.domain.shopping.entity.ShoppingSpree;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class ShoppingExpense extends Expense {

    @OneToOne(mappedBy = "shoppingExpense")
    private ShoppingSpree shoppingSpree;

    /**
     * Constructs a ShoppingExpense object representing a transaction in the bookkeeping system.
     *
     * @param amount The monetary value of the expense in the smallest currency unit (e.g., cents for USD).
     * @param description A brief description of the expense.
     * @param date The date and time the expense occurred.
     * @param recipient The recipient to whom the expense was paid.
     * @param account The account associated with the expense transaction.
     * @param shoppingSpree The ShoppingSpree that was paid with this expense.
     * @throws IllegalArgumentException if the shoppingSpree is invalid.
     */
    public ShoppingExpense(long amount, String description, LocalDateTime date, String recipient, Account account) throws IllegalArgumentException {
        super(amount, description, date, recipient, account);
        if (!validate(shoppingSpree)) {
            throw new IllegalArgumentException("Invalid ShoppingExpense object");
        }
        this.shoppingSpree = null;
    }

    /**
     * Sets the ShoppingSpree associated with the ShoppingExpense. If the ShoppingSpree is invalid, an exception is thrown. This method is mostly used
     * to work around the fact that either the ShoppingSpree or the ShoppingExpense must be persisted before the other can be set.
     *
     * @param shoppingSpree The ShoppingSpree to set.
     * @throws IllegalArgumentException if the ShoppingSpree is invalid.
     */
    private void setShoppingSpree(ShoppingSpree shoppingSpree) throws IllegalArgumentException{
        if (!validateShoppingSpree(shoppingSpree)) {
            throw new IllegalArgumentException("Invalid ShoppingExpense object");
        }
        this.shoppingSpree = shoppingSpree;
    }

    /**
     * Validates the state of the ShoppingExpense object. This method ensures that both the validation
     *
     * @param shoppingSpree The ShoppingSpree to validate.
     * @return True, if the ShoppingSpree is valid. False otherwise.
     */
    private boolean validate(ShoppingSpree shoppingSpree) {
        return validateShoppingSpree(shoppingSpree);
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
