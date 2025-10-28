package com.terfehr.homehub.domain.household.entity;

import com.terfehr.homehub.domain.household.exception.InvalidHouseholdException;
import com.terfehr.homehub.domain.household.exception.InvalidInvitationException;
import com.terfehr.homehub.domain.household.exception.InvalidUserException;
import com.terfehr.homehub.domain.shared.exception.InvalidNameException;
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
    private Set<Invitation> invitations;

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
    public Household(String name) throws InvalidNameException {
        if (!validate(name)) {
            throw new IllegalArgumentException("Invalid arguments for Household creation");
        }
        this.name = name;
        this.roommates = new HashSet<>();
        this.invitations = new HashSet<>();
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
     * @throws IllegalArgumentException If the given name is invalid.
     */
    public void changeName(String name) throws IllegalArgumentException {
        if (!validateName(name)) {
            throw new IllegalArgumentException("Invalid arguments for Household name");
        }
        this.name = name;
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
     * Invites a User to the household by creating, adding and returning an Invitation object.
     *
     * @param user the User to invite to the household.
     * @return The newly created and added Invitation.
     * @throws InvalidHouseholdException If the calling Household is unsufficient for creating an Invitation.
     * @throws InvalidUserException If the given User is insufficient to create an Invitation from it.
     */
    public Invitation invite(User user) throws InvalidUserException {
        if (!canInvite(user)) {
            throw new InvalidUserException("Invalid Invitation for this Household");
        }
        Invitation invitation = new Invitation(this, user);
        this.invitations.add(invitation);
        return invitation;
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
     * Checks if the household has only one roommate.
     *
     * @return True if the household has only one roommate, false otherwise.
     */
    public boolean isLastRoommate() {
        return this.roommates.size() == 1;
    }

    /**
     * Promotes the first roommate to admin. If the household has no roommates, does nothing.
     */
    public void promoteRandomUserToAdmin() {
        roommates.stream().findFirst().ifPresent(r -> r.setRole(Role.ROLE_ADMIN.name()));
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
     * Checks if the User can be invited. This is the case if the User
     * is neither part of the Household via a Roommate already nor has been Invited and is references by an existing Invitation.
     *
     * @param user The User to check for possible invitation.
     * @return True, if the Invitation can happen. False otherwise.
     */
    private boolean canInvite(User user) {
        return validateUser(user) && !isInvited(user) && !isPartOf(user);
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
     * Checks if the User has already been invited to this household.
     * Happens after the validation of the User itself.
     *
     * @param user The User to check for existing invitation.
     * @return True if the User already received an Invitation. False otherwise.
     * @throws InvalidUserException If the given User is invalid.
     */
    private boolean isInvited(User user) throws InvalidUserException {
        if (!validateUser(user)) {
            throw new InvalidUserException("The given User is invalid! It might be null!");
        }
        return invitations.stream().anyMatch(i -> i.getUser().equals(user));
    }

    /**
     * Checks if the User is already part of this household. This is the case
     * if a Roommate with said User exists.
     * Happens after the validation of the User itself.
     *
     * @param user The User to check for possible invitation.
     * @return True if the User already is part of the household. False otherwise.
     * @throws InvalidUserException If the given User is invalid.
     */
    private boolean isPartOf(User user) throws InvalidUserException {
        if (!validateUser(user)) {
            throw new InvalidUserException("The given User is invalid! It might be null!");
        }
        return roommates.stream().anyMatch(r -> r.getUser().equals(user));
    }

    /**
     * Validates the given User by checking if it is null.
     *
     * @param user The User to validate.
     * @return True, if it is valid. False otherwise.
     */
    private boolean validateUser(User user) {
        return user != null;
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
