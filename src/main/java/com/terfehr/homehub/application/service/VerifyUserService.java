package com.terfehr.homehub.application.service;

import com.terfehr.homehub.application.command.VerifyUserCommand;
import com.terfehr.homehub.application.dto.UserDTO;
import com.terfehr.homehub.application.exception.UserNotFoundException;
import com.terfehr.homehub.domain.household.entity.User;
import com.terfehr.homehub.application.event.UserVerifiedEvent;
import com.terfehr.homehub.application.event.payload.UserVerifiedEventPayload;
import com.terfehr.homehub.domain.shared.exception.InvalidVerificationCodeException;
import com.terfehr.homehub.domain.household.repository.UserRepositoryInterface;
import com.terfehr.homehub.domain.shared.exception.InvalidEventPayloadException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Transactional
public class VerifyUserService {

    private final UserRepositoryInterface userRepository;
    private final ApplicationEventPublisher publisher;

    /**
     * Executed the command that verifies a User with the contained verification code. It also publishes an Event that
     * informs about the successful verification of a User.
     *
     * @param cmd The VerifyUserCommand containing the verification code to validate.
     * @return A UserDTO object of the verified User.
     * @throws UserNotFoundException If the User with the given verification code does not exist.
     * @throws IllegalArgumentException If the passed User to the UserDTO is invalid.
     * @throws IllegalStateException If the User cannot be verified.
     * @throws InvalidVerificationCodeException If the verification code is expired.
     * @throws InvalidEventPayloadException If the event payload is invalid.
     */
    public UserDTO execute(VerifyUserCommand cmd) throws UserNotFoundException,
            IllegalStateException,
            IllegalArgumentException,
            InvalidVerificationCodeException,
            InvalidEventPayloadException {

        String verificationCode = cmd.verificationCode();

        User user = userRepository.findByVerificationCode(verificationCode)
                .orElseThrow(() -> new UserNotFoundException("There is no user with this verification code: " + verificationCode));

        user.enable();

        userRepository.save(user);
        UserDTO dto = new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getFirstName(), user.getLastName(), user.isEnabled());

        UserVerifiedEventPayload payload = new UserVerifiedEventPayload(user.getId(), user.getEmail(), user.getUsername(), user.isEnabled());
        UserVerifiedEvent event = new UserVerifiedEvent(this, payload);
        publisher.publishEvent(event);

        return dto;
    }
}
