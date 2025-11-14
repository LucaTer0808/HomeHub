package com.terfehr.homehub.application.service;

import com.terfehr.homehub.application.command.ChangeEmailCommand;
import com.terfehr.homehub.application.command.VerifyEmailChangeCommand;
import com.terfehr.homehub.application.dto.ChangeEmailDTO;
import com.terfehr.homehub.application.dto.UserDTO;
import com.terfehr.homehub.application.exception.UserNotFoundException;
import com.terfehr.homehub.domain.household.entity.User;
import com.terfehr.homehub.application.event.ChangeEmailEvent;
import com.terfehr.homehub.application.event.VerifyEmailChangeEvent;
import com.terfehr.homehub.application.event.payload.ChangeEmailEventPayload;
import com.terfehr.homehub.application.event.payload.VerifyEmailChangeEventPayload;
import com.terfehr.homehub.domain.shared.exception.InvalidChangeEmailCodeException;
import com.terfehr.homehub.domain.household.repository.UserRepositoryInterface;
import com.terfehr.homehub.domain.household.service.UserService;
import com.terfehr.homehub.domain.shared.exception.InvalidEventPayloadException;
import com.terfehr.homehub.infrastructure.service.AuthUserProvider;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Transactional
public class ChangeEmailService {

    private final AuthUserProvider authUserProvider;
    private final UserRepositoryInterface userRepository;
    private final UserService userService;
    private final ApplicationEventPublisher publisher;

    /**
     * Executes the ChangeEmailCommand by fetching the represented User from the Database, updating his email address,
     * generating a unique verification code and expiration date and then publishing an Event that informs about said update.
     *
     * @param cmd The Command to execute.
     * @return A ChangeEmailDTO containing the updated User as a DTO and the generated verification code and expiration date.
     * @throws UserNotFoundException If the User to update does not exist.
     * @throws IllegalArgumentException If some argument is invalid.
     * @throws InvalidEventPayloadException If the event payload is invalid.
     */
    public ChangeEmailDTO execute(ChangeEmailCommand cmd) throws UserNotFoundException, IllegalArgumentException, InvalidEventPayloadException {

        String username = authUserProvider.getUsername();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        userService.changeEmail(user, cmd.email());

        userRepository.save(user);

        ChangeEmailEventPayload payload = new ChangeEmailEventPayload(user.getId(), user.getEmail(), cmd.email(), user.getEmailChangeCode(), user.getEmailChangeCodeExpiration());
        ChangeEmailEvent event = new ChangeEmailEvent(this, payload);
        publisher.publishEvent(event);

        UserDTO userDto = new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getFirstName(), user.getLastName(), user.isEnabled());
        return new ChangeEmailDTO(userDto, user.getEmailChangeCode(), user.getEmailChangeCodeExpiration());
    }

    /**
     * Executes the given VerifyEmailChangeCommand by updating the User's email address by setting the current email address as the pending one.
     * The verification code and expiration date are then removed, an Event is published, and the updated User is returned.
     *
     * @param cmd The Command to execute.
     * @return The updated User as a DTO.
     * @throws UserNotFoundException If the User to update does not exist.
     * @throws IllegalStateException If the email change is not pending.
     * @throws InvalidChangeEmailCodeException If the code is expired.
     * @throws InvalidEventPayloadException If the event payload is invalid.
     */
    public UserDTO execute(VerifyEmailChangeCommand cmd) throws UserNotFoundException, IllegalStateException, InvalidChangeEmailCodeException, InvalidEventPayloadException {

        User user = userRepository.findByEmailChangeCode(cmd.emailChangeCode())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.verifyEmailChange();

        userRepository.save(user);

        VerifyEmailChangeEventPayload payload = new VerifyEmailChangeEventPayload(user.getId(), user.getEmail());
        VerifyEmailChangeEvent event = new VerifyEmailChangeEvent(this, payload);
        publisher.publishEvent(event);

        return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getFirstName(), user.getLastName(), user.isEnabled());
    }
}
