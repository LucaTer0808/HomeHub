package com.terfehr.homehub.application.service;

import com.terfehr.homehub.application.command.RegisterUserCommand;
import com.terfehr.homehub.application.dto.UserDTO;
import com.terfehr.homehub.application.exception.*;
import com.terfehr.homehub.domain.household.entity.User;
import com.terfehr.homehub.domain.household.event.UserRegisteredEvent;
import com.terfehr.homehub.domain.household.event.payload.UserRegisteredEventPayload;
import com.terfehr.homehub.domain.household.exception.InvalidEmailException;
import com.terfehr.homehub.domain.household.exception.InvalidUserException;
import com.terfehr.homehub.domain.household.repository.UserRepositoryInterface;
import com.terfehr.homehub.domain.household.service.UserService;
import com.terfehr.homehub.domain.shared.exception.InvalidDomainEventPayloadException;
import com.terfehr.homehub.domain.shared.exception.InvalidNameException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * ApplicationService that handles the logic for registering a new User.
 */
@Service
@AllArgsConstructor
@Transactional
public class RegisterUserService {

    private final UserRepositoryInterface  userRepository;
    private final UserService userService;
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
     * @throws InvalidDomainEventPayloadException If the event payload is invalid.
     * @throws InvalidUsernameException If the username is invalid.
     * @throws InvalidEmailException If the email is invalid.
     * @throws InvalidPasswordException If the password is invalid.
     * @throws InvalidNameException If the first or last name is invalid.
     */
    public UserDTO execute(RegisterUserCommand cmd) throws EmailAlreadyExistsException,
            UsernameAlreadyExistsException,
            InvalidDomainEventPayloadException,
            InvalidUsernameException,
            InvalidEmailException,
            InvalidPasswordException,
            InvalidNameException

    {
        User registeredUser = userService.create(cmd.username(), cmd.email(), cmd.password(), cmd.confirmPassword(), cmd.firstName(), cmd.lastName());

        userRepository.save(registeredUser);

        UserDTO registerdUserDTO = new UserDTO(registeredUser.getId(),
                registeredUser.getUsername(),
                registeredUser.getEmail(),
                registeredUser.getFirstName(),
                registeredUser.getLastName(),
                registeredUser.isEnabled());

        UserRegisteredEventPayload payload = new UserRegisteredEventPayload(registeredUser.getId(), registeredUser.getEmail(), registeredUser.getUsername());
        UserRegisteredEvent event = new UserRegisteredEvent(this, payload);
        publisher.publishEvent(event);

        return registerdUserDTO;
    }

}
