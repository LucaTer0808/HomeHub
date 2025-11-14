package com.terfehr.homehub.domain.household.entity;

import com.terfehr.homehub.domain.shared.exception.*;
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
     * @throws InvalidHouseholdNameException if the provided name fails validation
     */
    public Household(String name) throws InvalidHouseholdNameException {
        if (!validate(name)) {
            throw new InvalidHouseholdNameException("Invalid arguments for Household creation");
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
     * Adds an Account to the Household by creating it on the fly, adding it to its Accounts collection,
     * and then returning it.
     *
     * @param accountName The desired name of the account.
     * @param amount The initial amount of the account represented in the smallest currency unit, e.g., Cents.
     * @param currencyCode The desired CurrencyCode of the account, e.g. "EUR"
     * @return The newly created and added Account.
     * @throws InvalidAccountNameException If the provided accountName is invalid.
     * @throws InvalidCurrencyCodeException If the provided currencyCode is invalid.
     * @throws InvalidHouseholdException If the calling Household is unsufficient for creating an Account.
     */
    public Account addAccount(String accountName, long amount, String currencyCode) throws
            InvalidAccountNameException,
            InvalidCurrencyCodeException,
            InvalidHouseholdException
    {
        Account account = new Account(accountName, amount, currencyCode, this);
        this.accounts.add(account);
        return account;
    }

    /**
     * Adds a roommate to the current household.
     *
     * @param roommate the roommate to be added to the household.
     * @throws IllegalArgumentException If the given Roommate is invalid.
     */
    public void addRoommate(Roommate roommate) throws InvalidRoommateException {
        if (!canAddRoommate(roommate)) {
            throw new InvalidRoommateException("Invalid Roommate for this Household");
        }
        this.roommates.add(roommate);
    }

    /**
     * Sets the name of the Household and then returns the Household afterward. If the given name
     * is invalid, an exception is thrown.
     *
     * @param name The name to set.
     * @throws InvalidHouseholdNameException If the given name is invalid.
     */
    public void changeName(String name) throws InvalidHouseholdNameException {
        if (!validateName(name)) {
            throw new InvalidHouseholdNameException("Invalid arguments for Household name");
        }
        this.name = name;
    }

    /**
     * Deletes the provided Account from the Household if it can be deleted.
     *
     * @param account The Account to delete.
     * @throws InvalidAccountException If the provided Account is invalid or not associated with this household.
     */
    public void deleteAccount(Account account) throws InvalidAccountException {
        if (!canDeleteAccount(account)) {
            throw new InvalidAccountException("Invalid Account for this Household");
        }
        this.accounts.remove(account);
    }

    /**
     * Invites a User to the household by creating, adding, and returning an Invitation object.
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
     * Removes a specified Invitation from the current household if it can be removed.
     * If not, an exception is thrown.
     *
     * @param invitation the Invitation to be removed from the household.
     * @throws InvalidInvitationException if the provided Invitation is invalid or not associated with this household
     */
    public void removeInvitation(Invitation invitation) throws InvalidInvitationException {
        if (!canRemoveInvitation(invitation)) {
            throw new InvalidInvitationException("Invalid Invitation for this Household");
        }
        this.invitations.remove(invitation);
    }

    /**
     * Removes a specified roommate from the current household.
     * Validates the roommate's association with the household before removal.
     * If the Roommate was an Admin before leaving, the admin status is transferred to another Rommmate randomly.
     * Throws an exception if the roommate is invalid or not associated with the household.
     *
     * @param roommate the roommate to be removed from the household
     * @throws InvalidRoommateException if the provided roommate is invalid or not associated with this household
     */
    public void removeRoommate(Roommate roommate) throws InvalidRoommateException {
        if (!canRemoveRoommate(roommate)) {
            throw new InvalidRoommateException("Invalid Roommate for this Household");
        }
        this.roommates.remove(roommate);
        if (roommate.isAdmin()) {
            promoteRandomUserToAdmin();
        }
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
     * Determines if the given Account can be deleted. It has to be valid and be part of this household.
     *
     * @param account The Account to check for deletion.
     * @return True if the account can be deleted, false otherwise.
     */
    private boolean canDeleteAccount(Account account) {
        return validateAccount(account) && this.accounts.contains(account);
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
     * Checks if the given Roommate can be promoted to Admin. He has to be part of the Household, be not null and still be a User.
     *
     * @param roommate The Roommate to check for promotion.
     * @return True, if the Roommate can be promoted. False otherwise.
     */
    private boolean canPromoteRoommate(Roommate roommate) {
        return validateRoommate(roommate) && this.roommates.contains(roommate) && roommate.getRole().equals(Role.ROLE_USER);
    }

    /**
     * Checks if the given Invitation can be removed from the household. It has to be valid and be part of this household.
     * @param invitation The Invitation to check for removal
     * @return True, if the Invitation can be removed; false otherwise
     */
    private boolean canRemoveInvitation(Invitation invitation) {
        return validateInvitation(invitation) && this.invitations.contains(invitation);
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
        return validateRoommate(roommate) && this.roommates.contains(roommate) && this.roommates.size() > 1;
    }

    /**
     * Retrieves the current admin from the Household. There should ALWAYS be exactly one admin! If not, something is wrong!
     *
     * @return The current admin
     * @throws IllegalStateException If there is no administrating roommate! Should NEVER happen!
     */
    private Roommate getCurrentAdmin() throws IllegalStateException {
        return roommates.stream()
                .filter(Roommate::isAdmin)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("There is no current administrating roommate! This should NEVER happen!"));
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
     * Transfers the admin status from the old admin to the new admin.
     *
     * @param newAdmin The Roommate that should have admin rights.
     */
    public void transferAdminRights(Roommate newAdmin) {
        if (!canPromoteRoommate(newAdmin)) {
            throw new InvalidRoommateException("Invalid Roommate for this Household to promote to admin. Either he already is an admin or is not part of this household.");
        }
        Roommate oldAdmin = getCurrentAdmin();
        oldAdmin.setRole(Role.ROLE_USER.getValue());
        newAdmin.setRole(Role.ROLE_ADMIN.getValue());
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
     * Validates an Account by assuring it is not null.
     *
     * @param account The Account to validate.
     * @return True, if the Account is valid. False otherwise.
     */
    private boolean validateAccount(Account account) {
        return account != null;
    }

    /**
     * Validates the given Invitation. It has to be not null.
     * the household calling this method.
     *
     * @param invitation The Invitation to validate.
     * @return True, if the Invitation is valid. False otherwise.
     */
    private boolean validateInvitation(Invitation invitation) {
        return invitation != null;
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
     * Validates the given User by checking if it is null.
     *
     * @param user The User to validate.
     * @return True, if it is valid. False otherwise.
     */
    private boolean validateUser(User user) {
        return user != null;
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
