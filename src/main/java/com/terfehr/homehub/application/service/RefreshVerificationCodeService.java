package com.terfehr.homehub.application.service;

import com.terfehr.homehub.application.command.RefreshVerificationCodeCommand;
import com.terfehr.homehub.application.dto.RefreshVerificationCodeDTO;
import com.terfehr.homehub.application.dto.UserDTO;
import com.terfehr.homehub.application.exception.InvalidVerificationCodeExpirationException;
import com.terfehr.homehub.application.exception.UserNotFoundException;
import com.terfehr.homehub.domain.household.entity.User;
import com.terfehr.homehub.domain.household.event.RefreshVerificationCodeEvent;
import com.terfehr.homehub.domain.household.event.payload.RefreshVerificationCodeEventPayload;
import com.terfehr.homehub.domain.household.exception.InvalidVerificationCodeException;
import com.terfehr.homehub.domain.household.repository.UserRepositoryInterface;
import com.terfehr.homehub.domain.household.service.UserService;
import com.terfehr.homehub.domain.shared.exception.InvalidDomainEventPayloadException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Transactional
public class RefreshVerificationCodeService {

    private final ApplicationEventPublisher publisher;
    private final UserRepositoryInterface userRepository;
    private final UserService userService;

    /**
     * Executed the RefreshVerificationCodeCommand by fetching the represented User from the Database, refreshing his token
     * and then publishing an Event that informs about the said update.
     *
     * @param cmd The Command to execute.
     * @return A UserDTO representing the user of the refreshed code.
     * @throws UserNotFoundException If the User to update does not exist.
     * @throws InvalidVerificationCodeException If the verification code is expired.
     * @throws InvalidVerificationCodeExpirationException If the expiration of the verification code is invalid.
     * @throws InvalidDomainEventPayloadException If the event payload is invalid.
     */
    public RefreshVerificationCodeDTO execute(RefreshVerificationCodeCommand cmd) throws UserNotFoundException,
            InvalidVerificationCodeException,
            InvalidVerificationCodeExpirationException,
            InvalidDomainEventPayloadException {

        User user = userRepository.findByEmail(cmd.email())
                .orElseThrow(() -> new UserNotFoundException("There is no User with the email " + cmd.email()));

        userService.refreshVerificationCode(user);

        userRepository.save(user);

        UserDTO refreshedUser = new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getFirstName(), user.getLastName(), user.isEnabled());

        RefreshVerificationCodeEventPayload payload = new RefreshVerificationCodeEventPayload(user.getId(), user.getUsername(), user.getVerificationCode(), user.getVerificationCodeExpiration());
        RefreshVerificationCodeEvent event = new RefreshVerificationCodeEvent(this, payload);
        publisher.publishEvent(event);

        return new RefreshVerificationCodeDTO(refreshedUser, user.getVerificationCode(), user.getVerificationCodeExpiration());
    }
}
