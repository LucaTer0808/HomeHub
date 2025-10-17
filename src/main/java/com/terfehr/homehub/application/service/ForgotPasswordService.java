package com.terfehr.homehub.application.service;

import com.terfehr.homehub.application.command.ForgotPasswordCommand;
import com.terfehr.homehub.application.dto.ForgotPasswordDTO;
import com.terfehr.homehub.application.exception.UserNotFoundException;
import com.terfehr.homehub.domain.household.entity.User;
import com.terfehr.homehub.domain.household.event.ForgotPasswordEvent;
import com.terfehr.homehub.domain.household.event.payload.ForgotPasswordEventPayload;
import com.terfehr.homehub.domain.household.repository.UserRepositoryInterface;
import com.terfehr.homehub.domain.household.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ForgotPasswordService {

    private final ApplicationEventPublisher publisher;
    private final UserRepositoryInterface userRepository;
    private final UserService userService;

    public ForgotPasswordDTO execute(ForgotPasswordCommand cmd) {

        User user = userRepository.findByEmail(cmd.email())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String forgotPasswordCode = userService.generateUniqueForgotPasswordCode();
        LocalDateTime forgotPasswordCodeExpiration = userService.getForgotPasswordCodeExpiration();

        user.forgotPassword(forgotPasswordCode, forgotPasswordCodeExpiration);

        userRepository.save(user);

        ForgotPasswordEventPayload payload = new ForgotPasswordEventPayload(user.getId(),user.getEmail(), forgotPasswordCode, forgotPasswordCodeExpiration);
        ForgotPasswordEvent event = new ForgotPasswordEvent(this, payload);
        publisher.publishEvent(event);

        return new ForgotPasswordDTO(user.getEmail());
    }
}
