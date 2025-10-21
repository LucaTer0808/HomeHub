package com.terfehr.homehub.application.service;

import com.terfehr.homehub.application.command.ChangePasswordCommand;
import com.terfehr.homehub.application.dto.UserDTO;
import com.terfehr.homehub.application.exception.InvalidPasswordException;
import com.terfehr.homehub.application.exception.UserNotFoundException;
import com.terfehr.homehub.application.interfaces.AuthUserProviderInterface;
import com.terfehr.homehub.domain.household.entity.User;
import com.terfehr.homehub.domain.household.event.ChangePasswordEvent;
import com.terfehr.homehub.domain.household.event.payload.ChangePasswordEventPayload;
import com.terfehr.homehub.domain.household.repository.UserRepositoryInterface;
import com.terfehr.homehub.domain.household.service.UserService;
import com.terfehr.homehub.domain.shared.exception.InvalidEventPayloadException;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ChangePasswordService {

    private final ApplicationEventPublisher publisher;
    private final AuthUserProviderInterface userProvider;
    private final UserRepositoryInterface userRepository;
    private final UserService userService;

    /**
     * Executes the ChangePasswordCommand by fetching the represented User from the Database, updating his password and then publishing an Event that informs about the said update.
     *
     * @param cmd The Command to execute.
     * @return A UserDTO containing the updated User as a DTO. Since this is transferred to the client, the password itself is not included.
     * @throws AuthenticationCredentialsNotFoundException If there is no user in the security context.
     * @throws InvalidEventPayloadException If the event payload is invalid.
     * @throws UserNotFoundException If the User to update does not exist.
     * @throws InvalidPasswordException If the given password is invalid.
     */
    public UserDTO execute(ChangePasswordCommand cmd) throws AuthenticationCredentialsNotFoundException, InvalidEventPayloadException, UserNotFoundException, InvalidPasswordException {
        User user = userProvider.getUser();

        userService.setPassword(user, cmd.password());

        userRepository.save(user);

        ChangePasswordEventPayload payload = new ChangePasswordEventPayload(user.getId(), user.getUsername());
        ChangePasswordEvent event = new ChangePasswordEvent(this, payload);
        publisher.publishEvent(event);

        return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getFirstName(), user.getLastName(), user.isEnabled());
    }
}
