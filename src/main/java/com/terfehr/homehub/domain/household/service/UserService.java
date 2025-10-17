package com.terfehr.homehub.domain.household.service;

import com.terfehr.homehub.domain.household.repository.UserRepositoryInterface;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${registration.expiration_time}")
    private Integer verificationCodeExpirationTime; // Expressed in hours
    @Value("${change_email.expiration_time}")
    private Integer changeEmailCodeExpirationTime; // also expressed in hours
    @Value("${forgot_password.expiration_time}")
    private Integer forgotPasswordCodeExpirationTime; // also expressed in hours

    public UserService(UserRepositoryInterface userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Generates a unique verification code with UUID.
     *
     * @return The unique uuid sent to the User via mail.
     */
    public String generateUniqueVerificationCode() {
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
    public String generateUniqueEmailChangeCode() {
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
    public String generateUniqueForgotPasswordCode() {
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
    public LocalDateTime getVerificationCodeExpiration() {
        return LocalDateTime.now().plusHours(verificationCodeExpirationTime);
    }

    /**
     * Returns the time when the option to change the email has run out. The concrete value
     * can be found in the application.properties
     *
     * @return The LocalDateTime object.
     */
    public LocalDateTime getChangeEmailCodeExpiration() {
        return LocalDateTime.now().plusHours(changeEmailCodeExpirationTime);
    }

    /**
     * Returns the time when the option to change the password has run out. The concrete value
     * can be found in the application.properties
     *
     * @return The LocalDateTime object.
     */
    public LocalDateTime getForgotPasswordCodeExpiration() {
        return LocalDateTime.now().plusHours(forgotPasswordCodeExpirationTime);
    }

    /**
     * Determines if the Email is unique.
     *
     * @param email The email to check.
     * @return True, if the Email is unique. False otherwise.
     */
    public boolean isEmailUnique(String email) {
        return !userRepository.existsByEmail(email);
    }

    /**
     * Determines if the Username is unique.
     *
     * @param username The username to check.
     * @return True, if the username is unique.
     */
    public boolean isUsernameUnique(String username) {
        return !userRepository.existsByUsername(username);
    }
}
