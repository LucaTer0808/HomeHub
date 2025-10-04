package com.terfehr.homehub.domain.shopping.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Represents a single item in a shopping spree. In comparison to the ShoppingListItem class, it was deprived of its picked state.
 */
@Entity
@NoArgsConstructor
@Getter
public class ShoppingSpreeItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private int quantity;
    @ManyToOne
    private ShoppingSpree shoppingSpree;

    /**
     * Constructs a ShoppingSpreeItem object representing a single item in a shopping spree.
     * The @Embeddable annotation makes it a value object instead of a persistent entity.
     *
     * @param name The name of the item.
     * @param quantity The quantity of the item.
     */
    public ShoppingSpreeItem(String name, int quantity, ShoppingSpree shoppingSpree) throws IllegalArgumentException {
        if (!validate(name, quantity, shoppingSpree)) {
            throw new IllegalArgumentException("Invalid ShoppingSpreeItem object");
        }
        this.name = name;
        this.quantity = quantity;
        this.shoppingSpree = shoppingSpree;
    }

    /**
     * Validates the state of the ShoppingSpreeItem object by orchestrating the validation of the name, quantity and shoppingSpree.
     *
     * @param name The name of the item.
     * @param quantity The quantity of the item.
     * @param shoppingSpree The ShoppingSpree to which the item belongs.
     * @return True, if the parameters are valid. False otherwise.
     */
    private boolean validate(String name, int quantity, ShoppingSpree shoppingSpree) {
        return validateName(name) && validateQuantity(quantity) && validateShoppingSpree(shoppingSpree);
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
     * Validates the given shopping spree to ensure it is not null.
     *
     * @param shoppingSpree The shopping spree to be validated.
     * @return true if the shopping spree is not null, false otherwise.
     */
    private boolean validateShoppingSpree(ShoppingSpree shoppingSpree) {
        return shoppingSpree != null;
    }
}
