package com.terfehr.homehub.application.service;

import com.terfehr.homehub.application.command.RefreshVerificationCodeCommand;
import com.terfehr.homehub.application.dto.UserDTO;
import com.terfehr.homehub.application.exception.UserNotFoundException;
import com.terfehr.homehub.domain.household.entity.User;
import com.terfehr.homehub.domain.household.event.RefreshVerificationCodeEvent;
import com.terfehr.homehub.domain.household.event.payload.RefreshVerificationCodeEventPayload;
import com.terfehr.homehub.domain.household.repository.UserRepositoryInterface;
import com.terfehr.homehub.domain.household.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RefreshVerificationCodeService {

    private final ApplicationEventPublisher publisher;
    private final UserRepositoryInterface userRepository;
    private final UserService userService;

    /**
     * Executed the RefreshVerificationCodeCommand by fetching the represented User from the Database, refreshing his token
     * and then publishing an Event that informs about said update.
     *
     * @param cmd The Command to execute.
     * @return A UserDTO representing the user of the refreshed code.
     */
    public UserDTO execute(RefreshVerificationCodeCommand cmd) {

        User user = userRepository.findByEmail(cmd.email())
                .orElseThrow(() -> new UserNotFoundException("There is no User with the email " + cmd.email()));

        String newCode = userService.generateUniqueVerificationCode();
        LocalDateTime expiration = userService.getVerificationCodeExpiration();

        user.setVerificationCode(newCode);
        user.setVerificationCodeExpiration(expiration);
        userRepository.save(user);

        UserDTO refreshedUser = new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.isEnabled());

        RefreshVerificationCodeEventPayload payload = new RefreshVerificationCodeEventPayload(user.getId(), user.getUsername(), newCode, expiration);
        RefreshVerificationCodeEvent event = new RefreshVerificationCodeEvent(this, payload);
        publisher.publishEvent(event);

        return refreshedUser;
    }
}
