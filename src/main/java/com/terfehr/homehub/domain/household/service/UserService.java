package com.terfehr.homehub.domain.household.service;

import com.terfehr.homehub.domain.shared.exception.InvalidNameException;
import com.terfehr.homehub.application.exception.InvalidPasswordException;
import com.terfehr.homehub.application.exception.InvalidUsernameException;
import com.terfehr.homehub.application.exception.InvalidVerificationCodeExpirationException;
import com.terfehr.homehub.domain.household.entity.User;
import com.terfehr.homehub.domain.household.exception.*;
import com.terfehr.homehub.domain.household.repository.UserRepositoryInterface;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Service for preparing the registration of the user by generating the Verification Code as well as the
 * time of expiration for a possible verification.
 */
@Service
public class UserService {

    private final UserRepositoryInterface userRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${registration.expiration_time}")
    private Integer verificationCodeExpirationTime; // Expressed in hours
    @Value("${change_email.expiration_time}")
    private Integer changeEmailCodeExpirationTime; // also expressed in hours
    @Value("${forgot_password.expiration_time}")
    private Integer forgotPasswordCodeExpirationTime; // also expressed in hours

    public UserService(UserRepositoryInterface userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Changes the email of the User. The new email is validated and the code is generated.
     *
     * @param user The User to change the email for.
     * @param email The new email to set.
     * @throws InvalidEmailException If the email is invalid.
     * @throws InvalidChangeEmailCodeException If the code is invalid.
     * @throws InvalidChangeEmailCodeExpirationException If the expiration of the code is invalid.
     */
    public void changeEmail(User user, String email) throws InvalidEmailException, InvalidChangeEmailCodeException, InvalidChangeEmailCodeExpirationException {
        user.changeEmail(email, generateUniqueEmailChangeCode(), getChangeEmailCodeExpiration());
    }

    /**
     * Creates a new User in the database. The password is encoded before being saved.
     *
     * @param username The username of the User.
     * @param email The email of the User.
     * @param rawPassword The raw password of the User.
     * @param firstName The first name of the User.
     * @param lastName The last name of the User.
     * @return The newly created User.
     * @throws InvalidUsernameException If the username is invalid.
     * @throws InvalidEmailException If the email is invalid.
     * @throws InvalidPasswordException If the password is invalid.
     * @throws InvalidNameException If the first or last name is invalid.
     * @throws InvalidVerificationCodeException If the verification code is invalid.
     * @throws InvalidVerificationCodeExpirationException If the verification code expiration is invalid.
     */
    public User create(String username, String email, String rawPassword, String firstName, String lastName) throws
            InvalidUsernameException,
            InvalidEmailException,
            InvalidPasswordException,
            InvalidNameException,
            InvalidVerificationCodeException,
            InvalidVerificationCodeExpirationException
    {
        if (!isUsernameUnique(username)) {
            throw new InvalidUsernameException("The username " + username + " is already taken. Please choose another.");
        }

        if (!isEmailUnique(email)) {
            throw new InvalidEmailException("The email " + email + " is already taken. Please choose another.");
        }

        if (!validatePassword(rawPassword)) {
            throw new InvalidPasswordException("The password does not satisfy the requirements. Please chose a stronger password.");
        }

        String encodedPassword = passwordEncoder.encode(rawPassword);
        return new User(username, email, encodedPassword, firstName, lastName, generateUniqueVerificationCode(), getVerificationCodeExpiration());
    }

    /**
     * Handles the user forgot password flow. A new code as well as the expiration date are generated.
     *
     * @param user The User to handle the forgot password flow for.
     * @throws InvalidForgotPasswordCodeException If the code is invalid.
     * @throws InvalidForgotPasswordCodeExpirationException If the expiration of the code is invalid.
     */
    public void forgotPassword(User user) throws InvalidForgotPasswordCodeException, InvalidForgotPasswordCodeExpirationException {
        String newCode = generateUniqueForgotPasswordCode();
        LocalDateTime expiration = getForgotPasswordCodeExpiration();
        user.forgotPassword(newCode, expiration);
    }

    /**
     * Refreshes the verification code and its associated expiration date.
     * @param user The User to refresh the verification code for.
     */
    public void refreshVerificationCode(User user) throws InvalidVerificationCodeException, InvalidVerificationCodeExpirationException{
        String newCode = generateUniqueVerificationCode();
        LocalDateTime expiration = getVerificationCodeExpiration();
        user.refreshVerificationCode(newCode, expiration);
    }

    /**
     * Resets the password of the User. The new password is encoded before being saved and has to satisfy multiple
     * safety requirements.
     *
     * @param user The User to reset the password for.
     * @param password The new password to set.
     */
    public void resetPassword(User user, String password) {
        if (!validatePassword(password)) {
            throw new InvalidPasswordException("Invalid password");
        }
        String encodedPassword = passwordEncoder.encode(password);
        user.resetPassword(encodedPassword);
    }

    /**
     * Sets the password of the User. If the password is invalid, an exception is thrown.
     *
     * @param user The User to set the password for.
     * @param password The password to set.
     */
    public void setPassword(User user, String password) throws InvalidPasswordException {
        if (!validatePassword(password)) {
            throw new InvalidPasswordException("Invalid password");
        }
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
    }

    /**
     * Generates a unique verification code with UUID.
     *
     * @return The unique uuid sent to the User via mail.
     */
    private String generateUniqueVerificationCode() {
        String uuid;
        do {
            uuid = UUID.randomUUID().toString();
        } while (userRepository.existsByVerificationCode(uuid));
        return uuid;
    }

    /**
     * Generates a unique email change code with UUID.
     *
     * @return The unique uuid sent to the User via mail.
     */
    private String generateUniqueEmailChangeCode() {
        String uuid;
        do {
            uuid = UUID.randomUUID().toString();
        } while (userRepository.existsByEmailChangeCode(uuid));
        return uuid;
    }

    /**
     * Generates a unique forgot password code with UUID.
     *
     * @return The unique uuid sent to the User via mail.
     */
    private String generateUniqueForgotPasswordCode() {
        String uuid;
        do {
            uuid = UUID.randomUUID().toString();
        } while (userRepository.existsByForgotPasswordCode(uuid));
        return uuid;
    }

    /**
     * Returns the time when the option to verify the user has run out. The concrete value
     * can be found in the application.properties.
     *
     * @return The LocalDateTime object.
     */
    private LocalDateTime getVerificationCodeExpiration() {
        return LocalDateTime.now().plusHours(verificationCodeExpirationTime);
    }

    /**
     * Returns the time when the option to change the email has run out. The concrete value
     * can be found in the application.properties
     *
     * @return The LocalDateTime object.
     */
    private LocalDateTime getChangeEmailCodeExpiration() {
        return LocalDateTime.now().plusHours(changeEmailCodeExpirationTime);
    }

    /**
     * Returns the time when the option to change the password has run out. The concrete value
     * can be found in the application.properties
     *
     * @return The LocalDateTime object.
     */
    private LocalDateTime getForgotPasswordCodeExpiration() {
        return LocalDateTime.now().plusHours(forgotPasswordCodeExpirationTime);
    }

    /**
     * Determines if the Email is unique.
     *
     * @param email The email to check.
     * @return True, if the Email is unique. False otherwise.
     */
    private boolean isEmailUnique(String email) {
        return !userRepository.existsByEmail(email);
    }

    /**
     * Determines if the Username is unique.
     *
     * @param username The username to check.
     * @return True, if the username is unique.
     */
    private boolean isUsernameUnique(String username) {
        return !userRepository.existsByUsername(username);
    }

    /**
     * Validates the password. It has to meet the following criteria:
     * <ul>
     *     <li>Must be at least 8 characters long</li>
     *     <li>Must contain at least one uppercase letter</li>
     *     <li>Must contain at least one lowercase letter</li>
     *     <li>Must contain at least one number</li>
     *     <li>Must contain at least one special character</li>
     * </ul>
     *
     * @param password The password to be validated.
     * @return True, if the password is valid. False otherwise.
     */
    private boolean validatePassword(String password) {
        return password != null &&
                password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z\\d]).{8,}$");

    }
}
