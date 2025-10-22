package com.terfehr.homehub.domain.bookkeeping.entity;

import com.terfehr.homehub.domain.household.entity.Roommate;
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
     * @throws IllegalArgumentException if the shoppingSpree is invalid.
     */
    public ShoppingExpense(long amount, String description, LocalDateTime date, String recipient, Account account, Roommate roommate) throws IllegalArgumentException {
        super(amount, description, date, recipient, account, roommate);
        this.shoppingSpree = null;
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
