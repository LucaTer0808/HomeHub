package com.terfehr.homehub.application.service;

import com.terfehr.homehub.application.command.ChangeNameCommand;
import com.terfehr.homehub.application.dto.UserDTO;
import com.terfehr.homehub.application.interfaces.AuthUserProviderInterface;
import com.terfehr.homehub.domain.household.entity.User;
import com.terfehr.homehub.application.event.ChangeNameEvent;
import com.terfehr.homehub.application.event.payload.ChangeNameEventPayload;
import com.terfehr.homehub.domain.household.repository.UserRepositoryInterface;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Transactional
public class ChangeNameService {

    private final ApplicationEventPublisher publisher;
    private final UserRepositoryInterface userRepository;
    private final AuthUserProviderInterface userProvider;

    /**
     * Executes the ChangeNameCommand by fetching the represented User from the Database, updating his name and then publishing an Event that informs about the said update.
     *
     * @param cmd The Command to execute.
     * @return A UserDTO containing the updated User as a DTO. It already contains the updated first and last name.
     */
    public UserDTO execute(ChangeNameCommand cmd) {
        User user = userProvider.getUser();

        user.changeName(cmd.firstName(), cmd.lastName());

        userRepository.save(user);

        ChangeNameEventPayload payload = new ChangeNameEventPayload(user.getId(), cmd.firstName(), cmd.lastName());
        ChangeNameEvent event = new ChangeNameEvent(this, payload);
        publisher.publishEvent(event);

        return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getFirstName(), user.getLastName(), user.isEnabled());
    }
}
