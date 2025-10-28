package com.terfehr.homehub.application.service;

import com.terfehr.homehub.application.command.UserLoginCommand;
import com.terfehr.homehub.application.dto.UserDTO;
import com.terfehr.homehub.application.dto.UserLoginDTO;
import com.terfehr.homehub.domain.household.exception.InvalidPasswordException;
import com.terfehr.homehub.application.exception.UserNotFoundException;
import com.terfehr.homehub.application.interfaces.JwtServiceInterface;
import com.terfehr.homehub.domain.household.entity.User;
import com.terfehr.homehub.domain.household.event.UserLoginEvent;
import com.terfehr.homehub.domain.household.event.payload.UserLoginEventPayload;
import com.terfehr.homehub.domain.household.exception.InvalidUserException;
import com.terfehr.homehub.domain.household.repository.UserRepositoryInterface;
import com.terfehr.homehub.domain.shared.exception.InvalidDomainEventPayloadException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * ApplicationService that handles the logic for logging in a User.
 */
@Service
@AllArgsConstructor
@Transactional
public class UserLoginService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepositoryInterface userRepository;
    private final JwtServiceInterface jwtService;
    private final ApplicationEventPublisher publisher;

    /**
     * Executes the UserLoginCommand by validating the given credentials and generating a JWT token as well as
     * the date of issuance and expiration.
     * It also publishes an Event that informs about the successful login of a User that can be used for auditing
     * and monitoring purposes.
     *
     * @param cmd The UserLoginCommand containing the credentials to validate.
     * @return A UserLoginDTO containing the logged-in User and the generated JWT token.
     * @throws UserNotFoundException If the User with the given email address does not exist.
     * @throws InvalidPasswordException If the given password is incorrect.
     * @throws InvalidUserException If the given User to the UserDTO is invalid.
     * @throws InvalidDomainEventPayloadException If the event payload is invalid.
     */
    public UserLoginDTO execute(UserLoginCommand cmd) throws UserNotFoundException, InvalidPasswordException, InvalidUserException, InvalidDomainEventPayloadException {
        String email = cmd.email();
        String password = cmd.password();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("There is no user with this email " + email));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidPasswordException("The given password is incorrect");
        }

        UserDTO loggedInUser = new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getFirstName(), user.getLastName(), user.isEnabled());
        String jwtToken = jwtService.generateToken(user);
        Date createdAt = jwtService.getIssuedAt(jwtToken);
        Date expiresAt = jwtService.getExpiresAt(jwtToken);

        UserLoginDTO dto = new UserLoginDTO(loggedInUser, jwtToken, createdAt, expiresAt);

        UserLoginEventPayload payload = new UserLoginEventPayload(user.getId(), user.getUsername(), user.getEmail(), user.isEnabled());
        UserLoginEvent event = new UserLoginEvent(this, payload);
        publisher.publishEvent(event);

        return dto;
    }
}
