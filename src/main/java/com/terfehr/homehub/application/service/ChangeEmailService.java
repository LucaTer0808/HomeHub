package com.terfehr.homehub.application.service;

import com.terfehr.homehub.application.command.ChangeEmailCommand;
import com.terfehr.homehub.application.dto.ChangeEmailDTO;
import com.terfehr.homehub.application.dto.UserDTO;
import com.terfehr.homehub.application.exception.UserNotFoundException;
import com.terfehr.homehub.domain.household.entity.User;
import com.terfehr.homehub.domain.household.event.ChangeEmailEvent;
import com.terfehr.homehub.domain.household.event.payload.ChangeEmailEventPayload;
import com.terfehr.homehub.domain.household.repository.UserRepositoryInterface;
import com.terfehr.homehub.domain.household.service.UserService;
import com.terfehr.homehub.infrastructure.service.AuthUserProvider;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ChangeEmailService {

    private final AuthUserProvider authUserProvider;
    private final UserRepositoryInterface userRepository;
    private final UserService userService;
    private final ApplicationEventPublisher publisher;

    public ChangeEmailDTO execute(ChangeEmailCommand cmd) {

        String username = authUserProvider.getUsername();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String emailChangeToken = userService.generateUniqueEmailChangeCode();
        LocalDateTime emailChangeTokenExpiration = userService.getChangeEmailCodeExpiration();


        user.setPendingEmail(cmd.email());
        user.setEmailChangeToken(emailChangeToken);
        user.setEmailChangeTokenExpiration(emailChangeTokenExpiration);

        userRepository.save(user);

        ChangeEmailEventPayload payload = new ChangeEmailEventPayload(user.getId(), user.getEmail(), cmd.email(), emailChangeToken, emailChangeTokenExpiration);
        ChangeEmailEvent event = new ChangeEmailEvent(this, payload);
        publisher.publishEvent(event);

        UserDTO userDto = new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.isEnabled());
        return new ChangeEmailDTO(userDto, emailChangeToken, emailChangeTokenExpiration);
    }
}
