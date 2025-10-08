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
public class UserRegistrationService {

    private final UserRepositoryInterface userRepository;
    @Value("${registration.expiration_time}")
    private Integer expirationTime; // Expressed in hours

    public UserRegistrationService(UserRepositoryInterface userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Generates a unique verification code with UUID to enable the sending of
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
     * Returns the time when the option to verify the user has run out. The concrete value
     * can be found in the application.properties.
     *
     * @return The LocalDateTime object.
     */
    public LocalDateTime getVerificationCodeExpiration() {
        return LocalDateTime.now().plusHours(expirationTime);
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
