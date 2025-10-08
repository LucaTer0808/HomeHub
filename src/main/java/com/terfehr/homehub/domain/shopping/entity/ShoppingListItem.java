package com.terfehr.homehub.domain.shopping.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "shopping_list_items")
public class ShoppingListItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private boolean picked;

    @ManyToOne
    @JoinColumn(name = "shopping_list_id")
    private ShoppingList shoppingList;

    /**
     * Constructs a new ShoppingListItem object with the given parameters.
     *
     * @param name The name of the item.
     * @param quantity The Quantity of the item.
     * @param shoppingList The ShoppingList to which the item belongs.
     * @throws IllegalArgumentException If the given parameters are invalid.
     */
    public ShoppingListItem(String name, int quantity, ShoppingList shoppingList) throws IllegalArgumentException{
        if (!validate(name, quantity, shoppingList)) {
            throw new IllegalArgumentException("Invalid ShoppingListItem object");
        }
        this.name = name;
        this.quantity = quantity;
        this.shoppingList = shoppingList;
        this.picked = false;
    }

    /**
     * Sets the name of the ShoppingListItem. If the name is invalid, an exception is thrown.
     *
     * @param name The desired name for the ShoppingListItem.
     * @throws IllegalArgumentException If the given name is invalid.
     */
    public void setName(String name) throws IllegalArgumentException {
        if (!validateName(name)) {
            throw new IllegalArgumentException("Invalid ShoppingListItem object");
        }
        this.name = name;
    }

    /**
     * Sets the quantity of the ShoppingListItem. If the quantity is invalid, an exception is thrown.
     *
     * @param quantity The desired quantity for the ShoppingListItem.
     * @throws IllegalArgumentException If the given quantity is invalid.
     */
    public void setQuantity(int quantity) throws IllegalArgumentException {
        if (!validateQuantity(quantity)) {
            throw new IllegalArgumentException("Invalid quantity");
        }
        this.quantity = quantity;
    }

    /**
     * Marks the ShoppingListItem as picked.
     *
     * @throws IllegalStateException If the ShoppingListItem is already picked.
     */
    public void pick() throws IllegalStateException {
        if (this.picked) {
            throw new IllegalStateException("ShoppingListItem is already picked");
        }
        this.picked = true;
    }

    /**
     * Unmarks the ShoppingListItem as picked.
     *
     * @throws IllegalStateException If the ShoppingListItem is not picked.
     */
    public void unpick() {
        if (!this.picked) {
            throw new IllegalStateException("ShoppingListItem is not picked");
        }
        this.picked = false;
    }

    /**
     * Validates the given parameters by orchestrating to the internal validation methods.
     *
     * @param name The name to validate.
     * @param quantity The quantity to validate.
     * @param shoppingList The ShoppingList to validate.
     * @return True, if all parameters are valid. False otherwise.
     */
    private boolean validate(String name, int quantity, ShoppingList shoppingList) {
        return validateName(name) && validateQuantity(quantity) && validateShoppingList(shoppingList);
    }

    /**
     * Validates the given name to ensure it is not null or empty.
     *
     * @param name The name to be validated.
     * @return true if the name is not null and not empty, false otherwise.
     */
    private boolean validateName(String name) {
        return name != null && !name.isBlank();
    }

    /**
     * Validates the given quantity to ensure that it is greater than zero.
     *
     * @param quantity The quantity to be validated.
     * @return true if the quantity is greater than zero, false otherwise.
     */
    private boolean validateQuantity(int quantity) {
        return quantity > 0;
    }

    /**
     * Validates the given shopping list to ensure it is not null.
     *
     * @param shoppingList The ShoppingList to be validated.
     * @return true if the ShoppingList is not null, false otherwise.
     */
    private boolean validateShoppingList(ShoppingList shoppingList) {
        return shoppingList != null;
    }
}
