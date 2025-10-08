package com.terfehr.homehub.domain.household.entity;

import com.terfehr.homehub.domain.bookkeeping.entity.Account;
import com.terfehr.homehub.domain.scheduling.entity.TaskList;
import com.terfehr.homehub.domain.shopping.entity.ShoppingList;
import com.terfehr.homehub.domain.shopping.entity.ShoppingSpree;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
    private Set<ShoppingSpree> shoppingSprees;

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
        this.shoppingSprees = new HashSet<>();
    }

    /**
     * Sets the name of the Household and then returns the Household afterward. If the given name
     * is invalid, an exception is thrown.
     *
     * @param name The name to set.
     * @return The updated Household.
     * @throws IllegalArgumentException If the given name is invalid.
     */
    public Household setName(String name) throws IllegalArgumentException {
        if (!validateName(name)) {
            throw new IllegalArgumentException("Invalid arguments for Household name");
        }
        this.name = name;
        return this;
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
     * Adds a new Account to the Household by constructing it on the fly and then adding it to itself. It then returns
     * the account entity.
     *
     * @param name The name of the account.
     * @param amount The initial balance.
     * @param currencyCode The currency in which  the account operates
     *
     * @throws IllegalArgumentException If the given parameters are not valid to create an Account.
     */
    public Account addAccount(String name, long amount, String currencyCode) throws IllegalArgumentException {
        Account account = new Account(name, amount, currencyCode, this);
        this.accounts.add(account);
        return account;
    }

    /**
     * Removes the given Account from the Household if it is contained. An Exception is thrown otherwise.
     *
     * @param account The Account to remove.
     * @throws IllegalArgumentException If the Account is not part of the household.
     */
    public void removeAccount(Account account) throws IllegalArgumentException {
        if (!this.accounts.contains(account)) {
            throw new IllegalArgumentException("Invalid account for this Household");
        }
    }

    /**
     * Adds a new TaskList to the Household by building it on the fly and then returning it. If The given
     * parameter names are invalid, an exception is thrown.
     *
     * @param name The desired name for the TaskList.
     * @return The built and added TaskList.
     * @throws IllegalArgumentException If the given name is insufficient.
     */
    public TaskList addTaskList(String name) throws IllegalArgumentException {
        TaskList list =  new TaskList(name, this);
        this.taskLists.add(list);
        return list;
    }

    /**
     * Tries to remove the given TaskList from the Household. If it is invalid or not contained,
     * an exception is thrown.
     *
     * @param list The TaskList to remove from the Household.
     * @throws IllegalArgumentException If the TaskList can not be removed.
     */
    public void removeTaskList(TaskList list) throws IllegalArgumentException {
        if (!canRemoveTaskList(list)) {
            throw new IllegalArgumentException("Invalid task list for this Household");
        }
    }

    /**
     * Adds and returns a ShoppingList by constructing it on the fly, adding it to the Household and finally returning it.
     * If the given parameters are insufficient to construct a ShoppingList, an exception is thrown.
     *
     * @param name The desired name of the ShoppingList.
     * @return The created and added ShoppingList.
     * @throws IllegalArgumentException If the given parameters are insufficient for creating a ShoppingList.
     */
    public ShoppingList addShoppingList(String name) throws IllegalArgumentException {
        ShoppingList list = new ShoppingList(name, this);
        this.shoppingLists.add(list);
        return list;
    }

    /**
     * Tries to remove the given ShoppingList from the Household. If it can not be removed,
     * an exception is thrown.
     *
     * @param list The ShoppingList to remove
     * @throws IllegalArgumentException If the given ShoppingList can not be removed.
     */
    public void removeShoppingList(ShoppingList list) throws IllegalArgumentException {
        if (!canRemoveShoppingList(list)) {
            throw new  IllegalArgumentException("Invalid shopping list for this Household");
        }
        this.shoppingLists.remove(list);
    }

    /**
     * Adds a ShoppingSpree to the household by creating it on the fly and then returning it.
     *
     * @param date The time when the ShoppingSpree took place.
     * @return Returns the newly created ShoppingList.
     */
    public ShoppingSpree addShoppingSpree(LocalDateTime date) {
        ShoppingSpree spree = new ShoppingSpree(date, this);
        this.shoppingSprees.add(spree);
        return spree;
    }

    /**
     * removes a ShoppingSpree from the household. If the ShoppingSpree does not exist, an exception is thrown.
     *
     * @param spree The ShoppingSpree to remove.
     * @throws IllegalArgumentException If the ShoppingSpree does not exist.
     */
    public void removeShoppingSpree(ShoppingSpree spree) throws IllegalArgumentException {
        if (!shoppingSprees.contains(spree)) {
            throw new IllegalArgumentException("ShoppingSpree does not exist");
        }
        this.shoppingSprees.remove(spree);
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
     * Decides if the given TaskList can be removed from the Household. It has to be valid and contained by the household.
     *
     * @param list The TaskList to remove.
     * @return True, if it can be removed. False otherwise.
     */
    private boolean canRemoveTaskList(TaskList list) {
        return validateTaskList(list) && this.taskLists.contains(list);
    }

    /**
     * Decides if the given ShoppingLIst can be removed from the Household. It has to be valid and contained by the Household.
     *
     * @param list The ShoppingList to remove.
     * @return True, if the given ShoppingList can be removed. False otherwise.
     */
    private boolean canRemoveShoppingList(ShoppingList list) {
        return validateShoppingList(list) && this.shoppingLists.contains(list);
    }

    /**
     * Determines if the given ShoppingSpree can be added to this household. It has to be valid and not already
     * be part of this Household.
     *
     * @param spree The Spree to add.
     * @return True, if the ShoppingSpree can be added. False otherwise.
     */
    private boolean canAddShoppingSpree(ShoppingSpree spree) {
        return validateShoppingSpree(spree) && !shoppingSprees.contains(spree);
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
     * Validates the given Shopping Spree. It can not be null and the shopping sprees household has to be this.
     *
     * @param spree The ShoppingSpree to check.
     * @return True, if it is valid. False otherwise.
     */
    private boolean validateShoppingSpree(ShoppingSpree spree) {
        return spree != null && spree.getHousehold().equals(this);
    }

    /**
     * Validates the given TaskList. It has to be not null.
     *
     * @param list The TaskList to validate.
     * @return True, if the TaskList is valid. False otherwise.
     */
    private boolean validateTaskList(TaskList list) {
        return list != null;
    }

    /**
     * Validates the given ShoppingList. It has to be not null.
     *
     * @param list The desired list to validate.
     * @return True, if the ShoppingList is valid. False otherwise.
     */
    private boolean validateShoppingList(ShoppingList list) {
        return list != null;
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
