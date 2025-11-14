package com.terfehr.homehub.application.service;

import com.terfehr.homehub.application.dto.UserDTO;
import com.terfehr.homehub.application.exception.UserNotFoundException;
import com.terfehr.homehub.application.interfaces.AuthUserProviderInterface;
import com.terfehr.homehub.domain.household.entity.User;
import com.terfehr.homehub.domain.household.event.GetUserEvent;
import com.terfehr.homehub.domain.household.event.payload.GetUserEventPayload;
import com.terfehr.homehub.domain.shared.exception.InvalidEventPayloadException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Transactional
public class GetUserService {

    private final ApplicationEventPublisher publisher;
    private final AuthUserProviderInterface userProvider;

    /**
     * Executes the GetUserService by fetching the represented User from the Database and returning it as a UserDTO.
     * @return The UserDTO containing the fetched User.
     */
    public UserDTO execute() throws UserNotFoundException, AuthenticationCredentialsNotFoundException, InvalidEventPayloadException {
        User user = userProvider.getUser();

        GetUserEventPayload payload = new GetUserEventPayload(user.getId());
        GetUserEvent event = new GetUserEvent(this, payload);
        publisher.publishEvent(event);

        return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getFirstName(), user.getLastName(), user.isEnabled());
    }
}
