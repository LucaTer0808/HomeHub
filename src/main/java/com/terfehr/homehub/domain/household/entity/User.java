package com.terfehr.homehub.domain.household.entity;

import com.terfehr.homehub.domain.shared.exception.*;
import com.terfehr.homehub.domain.household.service.UserService;
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
@Table(name = "users")
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

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false)
    private boolean enabled;

    @Column(name = "verification_code", unique = true)
    private String verificationCode;

    @Column(name = "verification_code_expiration")
    private LocalDateTime verificationCodeExpiration;

    @Column(name = "pending_email")
    private String pendingEmail;

    @Column(name = "email_change_code", unique = true)
    private String emailChangeCode;

    @Column(name = "email_change_code_expiration")
    private LocalDateTime emailChangeCodeExpiration;

    @Column(name = "forgot_password_code", unique = true)
    private String forgotPasswordCode;

    @Column(name = "forgot_password_code_expiration")
    private LocalDateTime forgotPasswordCodeExpiration;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Roommate> roommates;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Invitation> invitations;

    /**
     * Constructs a new User object with the specified username, email, password,
     * verification code, and expiration time for the verification code. The user
     * is initialized with the "enabled" status set to false, and an empty set of roommates.
     * Note that the validations of all given attributes are rather of a technical nature
     * and should be handled in the service layer.
     *
     * @param username the username of the user, which must be unique.
     * @param email the email address of the user, which must be unique.
     * @param password the password for the user.
     * @param verificationCode the verification code assigned to the user for account activation.
     * @param verificationCodeExpiration the expiration date and time for the verification code.
     * @throws InvalidUsernameException if the username is invalid.
     * @throws InvalidEmailException if the email is invalid.
     * @throws InvalidPasswordException if the password is invalid.
     * @throws InvalidNameException if the first or last name is invalid.
     * @throws InvalidVerificationCodeException if the verification code is invalid.
     * @throws InvalidVerificationCodeExpirationException if the verification code expiration is invalid.
     */
     public User(String username, String email, String password, String firstName, String lastName, String verificationCode, LocalDateTime verificationCodeExpiration) throws
             InvalidUsernameException,
             InvalidEmailException,
             InvalidPasswordException,
             InvalidNameException,
             InvalidVerificationCodeException,
             InvalidVerificationCodeExpirationException

     {
         if (!validateUsername(username)) {
             throw new InvalidUsernameException("The given username does not satisfy the requirements");
         }

         if (!validateEmail(email)) {
             throw new InvalidEmailException("Invalid email");
         }

         if (!validatePassword(password)) {
             throw new InvalidPasswordException("Invalid password");
         }

         if (!validateNames(firstName, lastName)) {
             throw new InvalidNameException("Invalid names passed. Probably, one passed name is null");
         }

         if (!validateCode(verificationCode)) {
             throw new InvalidVerificationCodeException("Invalid verification code");
         }

         if (!validateExpiration(verificationCodeExpiration)) {
             throw new InvalidVerificationCodeExpirationException("Invalid verification code expiration");
         }

        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.enabled = false;
        this.verificationCode = verificationCode;
        this.verificationCodeExpiration = verificationCodeExpiration;
        this.pendingEmail = null;
        this.emailChangeCode = null;
        this.emailChangeCodeExpiration = null;
        this.forgotPasswordCode = null;
        this.forgotPasswordCodeExpiration = null;
        this.roommates = new HashSet<>();
        this.invitations = new HashSet<>();
    }

    /**
     * Adds a Roommate to the user's list of roommates. The Roommate must not yet be part of the user's list of roommates.
     *
     * @param roommate The Roommate to add.
     * @throws InvalidRoommateException If the Roommate is already part of the user's list of roommates or is null.
     */
    public void addRoommate(Roommate roommate) throws InvalidRoommateException {
        if (!canAddRoommate(roommate)) {
            throw new InvalidRoommateException("Roommate is already part of the user's list of roommates");
        }
        this.roommates.add(roommate);
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
     * Sets a pending email change for the user by setting the pending email, the code, and the expiration date to the provided values.
     *
     * @param newEmail The new email to set.
     * @param changeEmailCode The code to set.
     * @param changeEmailCodeExpiration The expiration timestamp to set.
     * @throws InvalidEmailException If the email is invalid.
     * @throws InvalidChangeEmailCodeException If the code is invalid.
     * @throws InvalidChangeEmailCodeExpirationException If the expiration of the code is invalid.
     */
    public void changeEmail(String newEmail, String changeEmailCode, LocalDateTime changeEmailCodeExpiration) throws InvalidEmailException, InvalidChangeEmailCodeException, InvalidChangeEmailCodeExpirationException{
        if (!validateEmail(newEmail)) {
            throw new InvalidEmailException("Invalid email");
        }
        if (!validateCode(changeEmailCode)) {
            throw new InvalidChangeEmailCodeException("Invalid verification code");
        }
        if (!validateExpiration(changeEmailCodeExpiration)) {
            throw new InvalidChangeEmailCodeExpirationException("Invalid verification code expiration");
        }
        this.pendingEmail = newEmail;
        this.emailChangeCode = changeEmailCode;
        this.emailChangeCodeExpiration = changeEmailCodeExpiration;
    }

    /**
     * Changes the name of the User by changing the first and last name simultaneously.
     *
     * @param firstName The new first name.
     * @param lastName The new last name.
     * @throws InvalidNameException If one of the names is invalid.
     */
    public void changeName(String firstName, String lastName) throws InvalidNameException {
        if (!validateNames(firstName, lastName)) {
            throw new InvalidNameException("Invalid names passed. Probably, one passed name is null");
        }
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Enables the user account by setting the "enabled" field to true.
     * This method clears any verification code and its associated expiration date.
     * The method ensures that the user can be enabled before performing the operation.
     *
     * @throws IllegalStateException if the user cannot be enabled
     */
    public void enable() throws IllegalStateException {
        if (enabled) {
            throw new IllegalStateException("User is enabled already");
        }
        if (verificationCodeExpiration.isBefore(LocalDateTime.now())) {
            throw new InvalidVerificationCodeException("Verification code expired");
        }
        this.enabled = true;
        this.verificationCode = null;
        this.verificationCodeExpiration = null;
    }

    /**
     * Called when the user forgot his password. Sets the forgot password code and its associated expiration date.
     *
     * @param forgotPasswordCode The forgot password code to set.
     * @param forgotPasswordCodeExpiration The expiration timestamp to set.
     * @throws InvalidForgotPasswordCodeException If the code is invalid.
     * @throws InvalidForgotPasswordCodeExpirationException If the expiration of the code is invalid.
     */
    public void forgotPassword(String forgotPasswordCode, LocalDateTime forgotPasswordCodeExpiration) throws InvalidForgotPasswordCodeException, InvalidForgotPasswordCodeExpirationException{
        if (!validateCode(forgotPasswordCode)) {
            throw new InvalidForgotPasswordCodeException("Invalid verification code");
        }

        if (!validateExpiration(forgotPasswordCodeExpiration)) {
            throw new InvalidForgotPasswordCodeExpirationException("Invalid verification code expiration");
        }
        this.forgotPasswordCode = forgotPasswordCode;
        this.forgotPasswordCodeExpiration = forgotPasswordCodeExpiration;
    }

    /**
     * Refreshes the verification code and its associated expiration date.
     *
     * @param verificationCode The new verification code to set.
     * @param verificationCodeExpiration The expiration timestamp to set.
     * @throws InvalidVerificationCodeException If the verification code is invalid.
     * @throws InvalidVerificationCodeExpirationException If the expiration of the code is invalid.
     */
    public void refreshVerificationCode(String verificationCode, LocalDateTime verificationCodeExpiration) throws InvalidVerificationCodeException, InvalidVerificationCodeExpirationException{
        if (!validateCode(verificationCode)) {
            throw new InvalidVerificationCodeException("Invalid verification code");
        }
        if (!validateExpiration(verificationCodeExpiration)) {
            throw new InvalidVerificationCodeExpirationException("Invalid verification code expiration");
        }
        this.verificationCode = verificationCode;
        this.verificationCodeExpiration = verificationCodeExpiration;
    }

    /**
     * Makes the User get invited to a Household by adding the given Invitation to its Invitation collection.
     *
     * @param invitation The Invitation to receive.
     * @throws InvalidInvitationException If the receivable Invitation is invalid. Should never happen since this case is covered by canReceiveInvitation().
     */
    public void receiveInvitation(Invitation invitation) throws InvalidInvitationException {
        if (!canReceiveInvitation(invitation)) {
            throw new InvalidInvitationException("Invalid invitation");
        }
        this.invitations.add(invitation);
    }

    /**
     * Removes an Invitation from the user's list of invitations. The Invitation must be part of the user's list of invitations and be valid.
     *
     * @param invitation The Invitation to remove.
     * @throws InvalidInvitationException If the Invitation is not part of the user's invitations or is null.
     */
    public void removeInvitation(Invitation invitation) throws InvalidInvitationException {
        if (!canRemoveInvitation(invitation)) {
            throw new InvalidInvitationException("Invitation is not part of the user's invitations or invalid. Might be null");
        }
        this.invitations.remove(invitation);
    }

    /**
     * Removes a Roommate from the user's list of roommates. The Roommate must be part of the user's list of roommates and be valid.
     *
     * @param roommate The Roommate to remove.
     * @throws InvalidRoommateException If the Roommate is not part of the user's list of roommates or is null.
     */
    public void removeRoommate(Roommate roommate) throws InvalidRoommateException {
        if (!canRemoveRoommate(roommate)) {
            throw new InvalidRoommateException("Roommate is not part of the user's list of roommates or invalid. Might be null");
        }
        this.roommates.remove(roommate);
    }

    /**
     * Resets the password of the user by setting the password to the provided value. It also sets the forgot password code and its associated expiration date to null.
     *
     * @param newPassword The new password to set.
     * @throws IllegalStateException If the password change is not pending.
     * @throws InvalidPasswordException If the password is invalid.
     * @throws InvalidForgotPasswordCodeExpirationException If the forgot password code has expired.
     */
    public void resetPassword(String newPassword) throws IllegalStateException, InvalidPasswordException, InvalidForgotPasswordCodeExpirationException {
        if (forgotPasswordCode == null || forgotPasswordCodeExpiration == null) {
            throw new IllegalStateException("No password change has been requested. Please apply for a new password change request.");
        }
        if (!validatePassword(newPassword)) {
            throw new InvalidPasswordException("Invalid password");
        }
        if (forgotPasswordCodeExpiration.isBefore(LocalDateTime.now())) {
            throw new InvalidForgotPasswordCodeExpirationException("Forgot password code expired. Please apply for another password change");
        }
        this.password = newPassword;
        this.forgotPasswordCode = null;
        this.forgotPasswordCodeExpiration = null;
    }

    /**
     * Sets the password of the user.
     *
     * @param password The new password to set.
     * @throws InvalidPasswordException If the password is invalid.
     */
    public void setPassword(String password) throws InvalidPasswordException {
        if (!validatePassword(password)) {
            throw new InvalidPasswordException("Invalid password");
        }
        this.password = password;
    }

    /**
     * Sets the username of the user.
     *
     * @param username The new username to set.
     * @throws InvalidUsernameException If the username is invalid.
     */
    public void setUsername(String username) throws InvalidUsernameException {
        if (!validateUsername(username)) {
            throw new InvalidUsernameException("The given username does not satisfy the requirements");
        }
        this.username = username;
    }

    /**
     * Verifies the email change request by setting the pending email to the current email, setting the pending email, the code, and the expiration date to null.
     *
     * @throws IllegalStateException if the email change is not pending
     * @throws InvalidChangeEmailCodeException if the code is expired
     */
    public void verifyEmailChange() throws IllegalStateException, InvalidChangeEmailCodeException {
        if (pendingEmail != null || emailChangeCode == null || emailChangeCodeExpiration == null) {
            throw new IllegalStateException("Email change is not pending");
        }
        if (emailChangeCodeExpiration.isBefore(LocalDateTime.now())) {
            throw new InvalidChangeEmailCodeException("Email change code expired");
        }
        this.email = pendingEmail;
        this.pendingEmail = null;
        this.emailChangeCode = null;
        this.emailChangeCodeExpiration = null;
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
     * Determines whether the User can receive the Invitation to the Household. This is the case if the
     * Invitation itself is valid and does not refer to a Household that the User was already invited to or is already
     * part of.
     *
     * @param invitation The Invitation to check for receival.
     * @return True, if the User can receive the Invitation. False otherwise.
     */
    private boolean canReceiveInvitation(Invitation invitation) {
        return validateInvitation(invitation) && !isInvited(invitation) && !isPartOf(invitation);
    }

    /**
     * Determines whether the User can remove the Invitation from his list of invitations.
     * This is the case if the Invitation itself is valid and is part of the User's list of invitations.
     *
     * @param invitation The Invitation to check for removal.
     * @return True, if the User can remove the Invitation. False otherwise.
     */
    private boolean canRemoveInvitation(Invitation invitation) {
        return validateInvitation(invitation) && this.invitations.contains(invitation);
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
     * Checks if the User was already invited to the Household referenced by the given Invitation. This happens after
     * the Invitation is validated.
     *
     * @param invitation The Invitation to be validated and checked for its Household.
     * @return True, if the User was already invited. False otherwise.
     * @throws InvalidInvitationException If the given Invitation is invalid.
     */
    private boolean isInvited(Invitation invitation) throws InvalidInvitationException {
        if (!validateInvitation(invitation)) {
            throw new InvalidInvitationException("Invalid invitation");
        }
        return invitations.stream().anyMatch(i -> i.getUser().equals(invitation.getUser()));
    }

    /**
     * Checks if the User was already added to the Household referenced by the given Invitation. This happens after
     * the Invitation is validated and is the case, if a Roommate with said Household exists.
     *
     * @param invitation The Invitation to be validated and checked for its Household.
     * @return True, if the User was already added. False otherwise.
     * @throws InvalidInvitationException If the given Invitation is invalid.
     */
    private boolean isPartOf(Invitation invitation) {
        if (!validateInvitation(invitation)) {
            throw new InvalidInvitationException("Invalid invitation");
        }
        return roommates.stream().anyMatch(r -> r.getUser().equals(invitation.getUser()));
    }

    /**
     * Validates the given Invitation by checking if it does not equal null.
     *
     * @param invitation The Invitation to validate.
     * @return True, if the given Invitation is valid. False otherwise.
     */
    private boolean validateInvitation(Invitation invitation) {
        return invitation != null;
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
     * Validates the username to check whether it is non-null and at least 5 characters long.
     *
     * @param username the username to be validated
     * @return true if the username is non-null and not empty; false otherwise
     */
    private boolean validateUsername(String username) {
        return username != null && username.length() >= 5;
    }

    /**
     * Validates the provided email address to check whether it is non-null and matches a certain regex.
     *
     * @param email the email address to be validated
     * @return true if the email is non-null and not regex-matching. false otherwise
     */
    private boolean validateEmail(String email) {
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email != null && email.matches(regex);
    }

    /**
     * Validates the provided password to check whether it is non-null and non-blank. Since this is the hashed password already,
     * the real check for sufficiency happens in {@link UserService}.
     *
     * @param password The password to be validated.
     * @return True, if the password is valid. False otherwise.
     */
    private boolean validatePassword(String password) {
        return password != null && !password.isBlank();
    }

    /**
     * Validates the given names of the user. They have to be non-null and non-blank.
     *
     * @param firstName The first name to validate.
     * @param lastName The last name to validate.
     * @return True, if the names are valid. False otherwise.
     */
    private boolean validateNames(String firstName, String lastName) {
        return firstName != null && !firstName.isBlank() && lastName != null && !lastName.isBlank();
    }

    /**
     * Validates the provided verification code to ensure that it is non-null and not empty.
     *
     * @param verificationCode the verification code to be validated
     * @return true if the verification code is non-null and not empty; false otherwise
     */
    private boolean validateCode(String verificationCode) {
        return verificationCode != null && !verificationCode.isEmpty();
    }

    /**
     * Validates the expiration date and time of a verification code.
     * This method checks whether the provided expiration timestamp is non-null.
     *
     * @param verificationCodeExpiration the expiration date and time of the verification code to validate
     * @return true if the expiration date and time is non-null; false otherwise
     */
    private boolean validateExpiration(LocalDateTime verificationCodeExpiration) {
        return verificationCodeExpiration != null;
    }
}
