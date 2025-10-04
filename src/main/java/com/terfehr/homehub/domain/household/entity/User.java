package com.terfehr.homehub.domain.household.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a user in the system with authentication and authorization details.
 * Implements UserDetails interface for integration with Spring Security.
 */
@Entity
@NoArgsConstructor
@Getter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean enabled;

    @Column(name = "verification_code")
    private String verificationCode;

    @Column(name = "verification_code_expiration")
    private LocalDateTime verificationCodeExpiration;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Roommate> roommates;

    /**
     * Constructs a new User object with the specified username, email, password,
     * verification code, and expiration time for the verification code. The user
     * is initialized with the "enabled" status set to false, and an empty set of roommates.
     * Note that the validations of all given attributes are rather of a technical nature
     * and should be handled in the service layer.
     *
     * @param username the username of the user, which must be unique
     * @param email the email address of the user, which must be unique
     * @param password the password for the user
     * @param verificationCode the verification code assigned to the user for account activation
     * @param verificationCodeExpiration the expiration date and time for the verification code
     */
     public User(String username, String email, String password, String verificationCode, LocalDateTime verificationCodeExpiration) throws IllegalArgumentException {
         if (!validate(username, email, password, verificationCode, verificationCodeExpiration)) {
             throw new IllegalArgumentException("Invalid arguments for User creation");
         }
        this.username = username;
        this.email = email;
        this.password = password;
        this.enabled = false;
        this.verificationCode = verificationCode;
        this.verificationCodeExpiration = verificationCodeExpiration;
        this.roommates = new HashSet<>();
    }

    /**
     * Adds a roommate to the current user's list of roommates after validating the input.
     * This method ensures that the roommate is valid and properly associated with the user.
     *
     * @param roommate the Roommate object to be added to the user's list of roommates
     *                 if it passes validation
     * @throws IllegalArgumentException if the provided roommate is invalid
     */
    public void addRoommate(Roommate roommate) throws IllegalArgumentException {
        if (!canAddRoommate(roommate)) {
            throw new IllegalArgumentException("Invalid Roommate for this User");
        }
        this.roommates.add(roommate);
    }

    /**
     * Removes a roommate from the user's list of roommates after validating the input.
     * This method ensures that the roommate is valid and currently associated with the user.
     *
     * @param roommate the Roommate object to be removed from the user's list of roommates
     * @throws IllegalArgumentException if the provided roommate is invalid or not associated with the user
     */
    public void removeRoommate(Roommate roommate) throws IllegalArgumentException {
        if (!canRemoveRoommate(roommate)) {
            throw new IllegalArgumentException("Invalid Roommate for this User");
        }
        this.roommates.remove(roommate);
    }

    /**
     * Retrieves the collection of granted authorities for the user.
     *
     * @return a collection of granted authorities associated with the user
     */
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    /**
     * Enables the user account by setting the "enabled" field to true.
     * This method clears any verification code and its associated expiration date.
     * The method ensures that the user can be enabled before performing the operation.
     *
     * @throws IllegalStateException if the user cannot be enabled
     */
    public void enable() throws IllegalArgumentException {
        if (!canBeEnabled()) {
            throw new IllegalStateException("User cannot be enabled");
        }
        this.enabled = true;
        this.verificationCode = null;
        this.verificationCodeExpiration = null;
    }

    /**
     * Validates the provided username, email, password, verification code, and
     * verification code expiration to ensure they meet required conditions.
     *
     * @param username the username to be validated
     * @param email the email address to be validated
     * @param password the password to be validated
     * @param verificationCode the verification code to be validated
     * @param verificationCodeExpiration the expiration date and time of the verification code to be validated
     * @return true if all parameters are valid; false otherwise
     */
    private boolean validate(String username, String email, String password, String verificationCode, LocalDateTime verificationCodeExpiration) {
        return validateUsername(username) && validateEmail(email) && validatePassword(password)
                && validateVerificationCode(verificationCode) && validateVerificationCodeExpiration(verificationCodeExpiration);
    }

    /**
     * Determines whether a given Roommate can be added to the user's list of roommates.
     * The method checks if the Roommate is valid and not already part of the user's roommates.
     *
     * @param roommate the Roommate object to be evaluated for addition
     * @return true if the Roommate can be added to the user's list of roommates; false otherwise
     */
    private boolean canAddRoommate(Roommate roommate) {
        return validateRoommate(roommate) && !this.roommates.contains(roommate);
    }

    /**
     * Determines whether a given Roommate can be removed from the user's list of roommates.
     * The method checks if the Roommate is valid and currently part of the user's list of roommates.
     *
     * @param roommate the Roommate object to evaluate for removal
     * @return true if the Roommate can be removed from the user's list of roommates; false otherwise
     */
    private boolean canRemoveRoommate(Roommate roommate) {
        return validateRoommate(roommate) && this.roommates.contains(roommate);
    }

    /**
     * Determines whether the user can be enabled.
     * A user can be enabled if they are currently disabled,
     * have a non-null verification code, a non-null verification code
     * expiration, and the current time is before the expiration.
     *
     * @return true if the user can be enabled; false otherwise
     */
    private boolean canBeEnabled() {
        return !this.enabled && LocalDateTime.now().isBefore(this.verificationCodeExpiration);
    }

    /**
     * Validates whether the provided Roommate is associated with the current User.
     *
     * @param roommate the Roommate object to validate
     * @return true if the Roommate is associated with the current User; false otherwise
     */
    private boolean validateRoommate(Roommate roommate) {
         return roommate != null && roommate.getUser().equals(this);
    }

    /**
     * Validates the username to check whether it is non-null and not empty.
     *
     * @param username the username to be validated
     * @return true if the username is non-null and not empty; false otherwise
     */
    private boolean validateUsername(String username) {
        return username != null && !username.isEmpty();
    }

    /**
     * Validates the provided email address to check whether it is non-null and not empty.
     *
     * @param email the email address to be validated
     * @return true if the email is non-null and not empty; false otherwise
     */
    private boolean validateEmail(String email) {
        return email != null && !email.isEmpty();
    }

    /**
     * Validates the provided password to ensure it is non-null and not empty.
     *
     * @param password the password to be validated
     * @return true if the password is non-null and not empty; false otherwise
     */
    private boolean validatePassword(String password) {
        return password != null && !password.isEmpty();
    }

    /**
     * Validates the provided verification code to ensure that it is non-null and not empty.
     *
     * @param verificationCode the verification code to be validated
     * @return true if the verification code is non-null and not empty; false otherwise
     */
    private boolean validateVerificationCode(String verificationCode) {
        return verificationCode != null && !verificationCode.isEmpty();
    }

    /**
     * Validates the expiration date and time of a verification code.
     * This method checks whether the provided expiration timestamp is non-null.
     *
     * @param verificationCodeExpiration the expiration date and time of the verification code to validate
     * @return true if the expiration date and time is non-null; false otherwise
     */
    private boolean validateVerificationCodeExpiration(LocalDateTime verificationCodeExpiration) {
        return verificationCodeExpiration != null;
    }
}
