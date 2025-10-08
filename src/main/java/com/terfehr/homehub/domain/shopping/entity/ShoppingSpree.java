package com.terfehr.homehub.domain.shopping.entity;

import com.terfehr.homehub.domain.bookkeeping.entity.ShoppingExpense;
import com.terfehr.homehub.domain.household.entity.Household;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "shopping_sprees")
public class ShoppingSpree {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "shoppingSpree", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ShoppingSpreeItem> shoppingSpreeItems;

    @Column(nullable = false)
    private LocalDateTime date;

    @OneToOne
    @JoinColumn(name = "shopping_expense_id")
    private ShoppingExpense shoppingExpense;

    @ManyToOne
    @JoinColumn(name = "household_id")
    private Household household;

    /**
     * Constructs a new ShoppingSpree object with the given parameters.
     *
     * @param date The date of the ShoppingSpree.
     * @param household The creating Household of the ShoppingSpree.
     * @throws IllegalArgumentException If the given parameters are invalid.
     */
    public ShoppingSpree(LocalDateTime date, Household household) throws IllegalArgumentException{
        if (!validate(date, household)) {
            throw new IllegalArgumentException("Invalid ShoppingSpree object");
        }
        this.date = date;
        this.shoppingSpreeItems = new HashSet<>();
        this.shoppingExpense = null;
        this.household = household;
    }

    /**
     * Sets the ShoppingExpense associated with the ShoppingSpree. If the ShoppingExpense is invalid, an exception is thrown.
     *
     * @param shoppingExpense The ShoppingExpense to set.
     * @throws IllegalStateException If the ShoppingExpense is invalid.
     */
    public void setShoppingExpense(ShoppingExpense shoppingExpense) throws IllegalStateException{
        if (!validateShoppingExpense(shoppingExpense)) {
            throw new IllegalStateException("Invalid ShoppingExpense object");
        }
        this.shoppingExpense = shoppingExpense;
    }

    /**
     * Adds a ShoppingSpreeItem to the ShoppingSpree. If the ShoppingSpreeItem is invalid, an exception is thrown.
     *
     * @param name The name of the ShoppingSpreeItem.
     * @param quantity The quantity of the ShoppingSpreeItem.
     * @throws IllegalArgumentException If the ShoppingSpreeItem is invalid.
     */
    public void addShoppingSpreeItem(String name, int quantity) throws IllegalArgumentException {
        this.shoppingSpreeItems.add(new ShoppingSpreeItem(name, quantity, this));
    }

    /**
     * Removes the given ShoppingSpreeItem from the ShoppingSpree or throws an exception if the ShoppingSpreeItem is not contained.
     *
     * @param shoppingSpreeItem The ShoppingSpreeItem to remove.
     * @throws IllegalArgumentException If the given ShoppingSpreeItem is not contained in the ShoppingSpree.
     */
    public void removeShoppingSpreeItem(ShoppingSpreeItem shoppingSpreeItem) throws IllegalArgumentException {
        if (!this.shoppingSpreeItems.contains(shoppingSpreeItem)) {
            throw new IllegalArgumentException("ShoppingSpree does not contain the given shoppingSpreeItem");
        }
        this.shoppingSpreeItems.remove(shoppingSpreeItem);
    }

    /**
     * Validates the given parameters by calling and combining the return value
     *
     * @param date The date of the ShoppingSpree.
     * @param household The creating Household of the ShoppingSpree.
     * @return True, if all parameters are valid. False otherwise.
     */
    private boolean validate(LocalDateTime date ,Household household) {
        return validateDate(date) && validateHousehold(household);
    }


    /**
     * Validates the given date to ensure it is non-null.
     *
     * @param date The date to be validated.
     * @return true if the date is non-null; false otherwise.
     */
    private boolean validateDate(LocalDateTime date) {
        return date != null;
    }

    /**
     * Validates the given shopping expense to ensure it is not null.
     *
     * @param shoppingExpense The shopping expense to be validated.
     * @return true if the shopping expense is not null; false otherwise.
     */
    private boolean validateShoppingExpense(ShoppingExpense shoppingExpense) {
        return shoppingExpense != null;
    }

    /**
     * Validates the given household to ensure it is not null.
     *
     * @param household The household to be validated.
     * @return true if the household is not null; false otherwise.
     */
    private boolean validateHousehold(Household household) {
        return household != null;
    }
}
