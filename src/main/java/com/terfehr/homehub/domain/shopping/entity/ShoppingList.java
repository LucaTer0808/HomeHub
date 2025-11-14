package com.terfehr.homehub.domain.shopping.entity;

import com.terfehr.homehub.domain.household.entity.Household;
import com.terfehr.homehub.domain.shared.exception.InvalidHouseholdException;
import com.terfehr.homehub.domain.shared.exception.InvalidShoppingListNameException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "shopping_lists")
public class ShoppingList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "shoppingList", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<ShoppingListItem> shoppingListItems;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "household_id")
    private Household household;

    /**
     * Constructs a new ShoppingList object with the given name. If the name is invalid, an exception is thrown.
     *
     * @param name The desired name for the ShoppingList.
     * @throws IllegalArgumentException If the given name is invalid.
     */
    public ShoppingList(String name, Household household) throws IllegalArgumentException{
        if (!validateName(name)) {
            throw new InvalidShoppingListNameException("Invalid ShoppingList name given");
        }
        if (!validateHousehold(household)) {
            throw new InvalidHouseholdException("The given Household is invalid");
        }
        this.name = name;
        this.shoppingListItems = new HashSet<>();
        this.household = household;
    }

    /**
     * Adds a ShoppingListItem to the ShoppingList.
     *
     * @param name The name of the ShoppingListItem.
     * @param quantity The quantity of the ShoppingListItem.
     * @throws IllegalArgumentException If the given parameters are invalid.
     */
    public void addItem(String name, int quantity) throws IllegalArgumentException {
        this.shoppingListItems.add(new ShoppingListItem(name, quantity, this));
    }

    /**
     * Removes the given ShoppingListItem from the ShoppingList or throws an exception if the ShoppingListItem is not contained.
     *
     * @param shoppingListItem The ShoppingListItem to remove.
     * @throws IllegalArgumentException If the given ShoppingListItem is not contained in the ShoppingList.
     */
    public void removeItem(ShoppingListItem shoppingListItem) throws IllegalArgumentException {
        if (!this.shoppingListItems.contains(shoppingListItem)) {
            throw new IllegalArgumentException("ShoppingList does not contain the given shoppingListItem");
        }
        this.shoppingListItems.remove(shoppingListItem);
    }

    /**
     * Gets the set of Items that are marked as picked.
     *
     * @return The set of picked Items.
     */
    public Set<ShoppingListItem> getPickedItems() {
        return shoppingListItems.stream().filter(ShoppingListItem::isPicked).collect(Collectors.toSet());
    }

    /**
     * Deletes all Items that are marked as picked.
     */
    public void deletePickedItems() {
        shoppingListItems.removeIf(ShoppingListItem::isPicked);
    }

    /**
     * Validates the given parameters by orchestrating to the internal validation methods.
     *
     * @param name The name to validate.
     * @return True, if all parameters are valid. False otherwise.
     */
    private boolean validate(String name, Household household) {
        return validateName(name) && validateHousehold(household);
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
     * Validates the given household to ensure it is not null.
     *
     * @param household The household to be validated.
     * @return True if the household is not null, false otherwise.
     */
    private boolean validateHousehold(Household household) {
        return household != null;
    }
}
