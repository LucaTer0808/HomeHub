package com.terfehr.homehub.domain.household.repository;

import com.terfehr.homehub.domain.household.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * <p>Repository interface for managing {@link User} entities in the persistence layer.
 * Extends {@link JpaRepository} to provide standard CRUD operations and additional
 * methods for querying user data based on specific attributes.</p>
 *
 * <p>The repository is annotated with {@link Repository}, making it a candidate for Spring's
 * component scanning to detect and register it as a Spring Bean.</p>
 */
@Repository
public interface UserRepositoryInterface extends JpaRepository<User, Long> {

    /**
     * Retrieves an Optional containing a User entity based on the provided username.
     * If no user is found with the given username, returns an empty Optional.
     *
     * @param username the username of the User to be retrieved
     * @return an Optional containing the User entity if found, otherwise an empty Optional
     */
    Optional<User> findByUsername(String username);

    /**
     * Retrieves an Optional containing a User entity based on the provided email.
     * If no user is found with the given email, returns an empty Optional.
     *
     * @param email the email address of the User to be retrieved
     * @return an Optional containing the User entity if found, otherwise an empty Optional
     */
    Optional<User> findByEmail(String email);

    /**
     * Retrieves an Optional containing a User entity based on the provided verification code.
     * If no user is found with the given verification code, returns an empty Optional.
     *
     * @param verificationCode the verification code of the User to be retrieved
     * @return an Optional containing the User entity if found, otherwise an empty Optional
     */
    Optional<User> findByVerificationCode(String verificationCode);

    /**
     * Decides whether a User with the given Verification Code exists in the Database. Primarily used to
     * create a unique UUID Verification Code.
     *
     * @param verificationCode The code to check.
     * @return True, if there is a User in the Database with said code. False otherwise.
     */
    boolean existsByVerificationCode(String verificationCode);

    /**
     * Decides whether a User with the given username exists in the Database.
     *
     * @param username The username to check.
     * @return True, if such a User exists. False otherwise.
     */
    boolean existsByUsername(String username);

    /**
     * Decides whether a User with the given Email exists in the Database.
     *
     * @param email The email to check.
     * @return True, if such a User exists. False otherwise
     */
    boolean existsByEmail(String email);
}
