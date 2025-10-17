package com.terfehr.homehub.application.service;

import com.terfehr.homehub.application.command.RegisterUserCommand;
import com.terfehr.homehub.application.dto.UserDTO;
import com.terfehr.homehub.application.exception.EmailAlreadyExistsException;
import com.terfehr.homehub.application.exception.UsernameAlreadyExistsException;
import com.terfehr.homehub.domain.household.entity.User;
import com.terfehr.homehub.domain.household.event.UserRegisteredEvent;
import com.terfehr.homehub.domain.household.event.payload.UserRegisteredEventPayload;
import com.terfehr.homehub.domain.household.exception.InvalidUserException;
import com.terfehr.homehub.domain.household.repository.UserRepositoryInterface;
import com.terfehr.homehub.domain.household.service.UserService;
import com.terfehr.homehub.domain.shared.exception.InvalidEventPayloadException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * ApplicationService that handles the logic for registering a new User.
 */
@Service
@AllArgsConstructor
@Transactional
public class RegisterUserService {

    private final UserRepositoryInterface  userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher publisher;

    /**
     * Executed the command that asks for the registration of a User with the contained credentials.
     * Encodes the contained password, generates the verification code and expiration date, checks for uniqueness of username
     * and email address, persists a new User object in the Database and publishes an Event that informs about the creation of a new user.
     *
     * @param cmd The command containing the User credentials.
     * @return A UserDTO containing the ID, username and password of the newly created user.
     * @throws EmailAlreadyExistsException If the email address is already taken.
     * @throws UsernameAlreadyExistsException IF the username is already taken.
     * @throws InvalidUserException If the given user to the UserDTO is invalid.
     * @throws InvalidEventPayloadException If the event payload is invalid.
     */
    public UserDTO execute(RegisterUserCommand cmd) throws EmailAlreadyExistsException, UsernameAlreadyExistsException, InvalidUserException, InvalidEventPayloadException {
        String username = cmd.username();
        String email =  cmd.email();
        String password = passwordEncoder.encode(cmd.password());
        String verificationCode = userService.generateUniqueVerificationCode();
        LocalDateTime expiration = userService.getVerificationCodeExpiration();

        if (!userService.isEmailUnique(email)) {
            throw new EmailAlreadyExistsException("The given email " + email + " already exists");
        }
        if (!userService.isUsernameUnique(username)) {
            throw new UsernameAlreadyExistsException("The given username " + username + " already exists");
        }

        User registeredUser = new User(username, email, password, verificationCode, expiration);
        userRepository.save(registeredUser);

        UserDTO registerdUserDTO = new UserDTO(registeredUser.getId(), registeredUser.getUsername(), registeredUser.getEmail(), registeredUser.isEnabled());

        UserRegisteredEventPayload payload = new UserRegisteredEventPayload(registeredUser.getId(), registeredUser.getEmail(), registeredUser.getUsername());
        UserRegisteredEvent event = new UserRegisteredEvent(this, payload);
        publisher.publishEvent(event);

        return registerdUserDTO;
    }

}
