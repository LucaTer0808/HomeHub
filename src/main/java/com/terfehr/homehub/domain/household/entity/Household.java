package com.terfehr.homehub.domain.household.entity;

import com.terfehr.homehub.domain.bookkeeping.entity.Account;
import com.terfehr.homehub.domain.scheduling.entity.TaskList;
import com.terfehr.homehub.domain.shopping.entity.ShoppingList;
import com.terfehr.homehub.domain.shopping.entity.ShoppingSpree;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>Represents a household entity that can contain multiple roommates.
 * The household has a unique identifier, a name, and a set of associated roommates.
 * It provides functionality to add and remove roommates while ensuring
 * validation of input and integrity of the entity's state.</p>
 *
 * <p>This class is marked as an entity for ORM (Object-Relational Mapping) purposes
 * and leverages JPA annotations for managing persistence.</p>
 */
@Entity
@NoArgsConstructor
@Getter
@Table(name = "households")
public class Household {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "household", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Roommate> roommates;

    @OneToMany(mappedBy = "household", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Account> accounts;

    @OneToMany(mappedBy = "household", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TaskList> taskLists;

    @OneToMany(mappedBy = "household", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ShoppingList> shoppingLists;

    @OneToMany(mappedBy = "household", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ShoppingSpree> shoppingSpree;

    /**
     * Constructs a new Household with the specified name.
     * The provided name must pass validation, otherwise an exception is thrown.
     *
     * @param name the name of the household; must be non-null and not blank
     * @throws IllegalArgumentException if the provided name fails validation
     */
    public Household(String name) throws IllegalArgumentException {
        if (!validate(name)) {
            throw new IllegalArgumentException("Invalid arguments for Household creation");
        }
        this.name = name;
        this.roommates = new HashSet<>();
        this.accounts = new HashSet<>();
        this.taskLists = new HashSet<>();
        this.shoppingLists = new HashSet<>();
    }

    /**
     * Adds a roommate to the current household.
     *
     * @param roommate the roommate to be added to the household.
     * @throws IllegalArgumentException If the given Roommate is invalid.
     */
    public void addRoommate(Roommate roommate) throws IllegalArgumentException {
        if (!canAddRoommate(roommate)) {
            throw new IllegalArgumentException("Invalid Roommate for this Household");
        }
        this.roommates.add(roommate);
    }

    /**
     * Removes a specified roommate from the current household.
     * Validates the roommate's association with the household before removal.
     * Throws an exception if the roommate is invalid or not associated with the household.
     *
     * @param roommate the roommate to be removed from the household
     * @throws IllegalArgumentException if the provided roommate is invalid or not associated with this household
     */
    public void removeRoommate(Roommate roommate) throws IllegalArgumentException {
        if (!canRemoveRoommate(roommate)) {
            throw new IllegalArgumentException("Invalid Roommate for this Household");
        }
        this.roommates.remove(roommate);
    }

    /**
     * Adds a ShoppingSpree to the household. If the ShoppingSpree is already contained in the household,
     * an exception is thrown.
     *
     * @param spree The ShoppingSpree to add.
     * @throws IllegalArgumentException If the ShoppingSpree is already contained in the household.
     */
    public void addShoppingSpree(ShoppingSpree spree) throws IllegalArgumentException {
        if (shoppingSpree.contains(spree)) {
            throw new IllegalArgumentException("ShoppingSpree already exists");
        }
        this.shoppingSpree.add(spree);
    }

    /**
     * removes a ShoppingSpree from the household. If the ShoppingSpree does not exist, an exception is thrown.
     *
     * @param spree The ShoppingSpree to remove.
     * @throws IllegalArgumentException If the ShoppingSpree does not exist.
     */
    public void removeShoppingSpree(ShoppingSpree spree) throws IllegalArgumentException {
        if (!shoppingSpree.contains(spree)) {
            throw new IllegalArgumentException("ShoppingSpree does not exist");
        }
        this.shoppingSpree.remove(spree);
    }

    /**
     * Validates the provided name by delegating to the validateName method.
     *
     * @param name the name to validate; must be non-null and not blank
     * @return true if the name is valid; false otherwise
     */
    private boolean validate(String name) {
        return validateName(name);
    }

    /**
     * Determines whether a specified roommate can be added to the household.
     * A roommate can be added if it is valid and does not already exist in the household.
     *
     * @param roommate the roommate to validate for addition
     * @return true if the roommate can be added; false otherwise
     */
    private boolean canAddRoommate(Roommate roommate) {
        return validateRoommate(roommate) && !this.roommates.contains(roommate);
    }

    /**
     * Determines whether a specified roommate can be removed from the household.
     * A roommate can be removed if they are associated with the household and the household
     * contains more than one roommate.
     *
     * @param roommate the roommate to validate for removal
     * @return true if the roommate can be removed; false otherwise
     */
    private boolean canRemoveRoommate(Roommate roommate) {
        return this.roommates.contains(roommate) && this.roommates.size() > 1;
    }

    /**
     * Validates whether the provided roommate is associated with the current household.
     * A roommate is considered valid if it is not null and its associated household matches
     * the current household instance.
     *
     * @param roommate the Roommate instance to validate
     * @return true if the roommate is associated with the current household, false otherwise
     */
    private boolean validateRoommate(Roommate roommate) {
        return roommate != null && roommate.getHousehold().equals(this);
    }

    /**
     * Validates the provided name to ensure it is non-null and not blank.
     *
     * @param name the name to validate
     * @return true if the name is non-null and not blank; false otherwise
     */
    private boolean validateName(String name) {
        return name != null && !name.isBlank();
    }
}
