package com.terfehr.homehub.application.service;

import com.terfehr.homehub.application.command.ChangeUsernameCommand;
import com.terfehr.homehub.application.dto.ChangeUsernameDTO;
import com.terfehr.homehub.application.dto.UserDTO;
import com.terfehr.homehub.application.exception.InvalidUsernameException;
import com.terfehr.homehub.application.interfaces.AuthUserProviderInterface;
import com.terfehr.homehub.application.interfaces.JwtServiceInterface;
import com.terfehr.homehub.domain.household.entity.User;
import com.terfehr.homehub.domain.household.event.ChangeUsernameEvent;
import com.terfehr.homehub.domain.household.event.payload.ChangeUsernameEventPayload;
import com.terfehr.homehub.domain.household.repository.UserRepositoryInterface;
import com.terfehr.homehub.domain.shared.exception.InvalidDomainEventPayloadException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
@Transactional
public class ChangeUsernameService {

    private final ApplicationEventPublisher publisher;
    private final AuthUserProviderInterface userProvider;
    private final JwtServiceInterface jwtService;
    private final UserRepositoryInterface userRepository;

    /**
     * Executes the ChangeUsernameCommand by fetching the represented User from the Database, updating his username and then publishing an Event that informs about the said update.
     *
     * @param cmd The Command to execute.
     * @return The ChangeUsernameDTO containing the updated User as a DTO and the generated JWT token as well as the date of issuance and expiration.
     * @throws InvalidUsernameException If the username is invalid.
     * @throws InvalidDomainEventPayloadException If the Event Payload is invalid.
     */
    public ChangeUsernameDTO execute(ChangeUsernameCommand cmd) throws InvalidUsernameException, InvalidDomainEventPayloadException {
        User user = userProvider.getUser();

        user.setUsername(cmd.username());

        userRepository.save(user);

        ChangeUsernameEventPayload payload = new ChangeUsernameEventPayload(user.getId(), user.getUsername());
        ChangeUsernameEvent event = new ChangeUsernameEvent(this, payload);
        publisher.publishEvent(event);

        UserDTO userDto = new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getFirstName(), user.getLastName(), user.isEnabled());
        String jwtToken = jwtService.generateToken(user);
        Date createdAt = jwtService.getIssuedAt(jwtToken);
        Date expiresAt = jwtService.getExpiresAt(jwtToken);

        return new ChangeUsernameDTO(userDto, jwtToken, createdAt, expiresAt);
    }
}
