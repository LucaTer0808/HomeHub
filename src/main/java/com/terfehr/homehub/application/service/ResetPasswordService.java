package com.terfehr.homehub.application.service;

import com.terfehr.homehub.application.command.ResetPasswordCommand;
import com.terfehr.homehub.application.dto.UserDTO;
import com.terfehr.homehub.application.exception.UserNotFoundException;
import com.terfehr.homehub.domain.household.entity.User;
import com.terfehr.homehub.domain.household.event.ResetPasswordEvent;
import com.terfehr.homehub.domain.household.event.payload.ResetPasswordEventPayload;
import com.terfehr.homehub.domain.household.repository.UserRepositoryInterface;
import com.terfehr.homehub.domain.household.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Transactional
public class ResetPasswordService {

    private final ApplicationEventPublisher publisher;
    private final UserRepositoryInterface userRepository;
    private final UserService userService;

    public UserDTO execute(ResetPasswordCommand cmd) {
        User user = userRepository.findByForgotPasswordCode(cmd.forgotPasswordCode())
                .orElseThrow(() -> new UserNotFoundException("There is no user with forgot password code: " + cmd.forgotPasswordCode()));

        userService.resetPassword(user, cmd.password());

        userRepository.save(user);

        ResetPasswordEventPayload payload = new ResetPasswordEventPayload(user.getId(), user.getEmail(), user.getPassword());
        ResetPasswordEvent event = new ResetPasswordEvent(this, payload);
        publisher.publishEvent(event);

        return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getFirstName(), user.getLastName(), user.isEnabled());
    }
}
