package com.terfehr.homehub.application.service;

import com.terfehr.homehub.application.command.CreateHouseholdCommand;
import com.terfehr.homehub.application.dto.HouseholdDTO;
import com.terfehr.homehub.application.exception.UserNotFoundException;
import com.terfehr.homehub.application.interfaces.AuthUserProviderInterface;
import com.terfehr.homehub.domain.household.entity.Household;
import com.terfehr.homehub.domain.household.entity.Roommate;
import com.terfehr.homehub.domain.household.entity.User;
import com.terfehr.homehub.application.event.CreateHouseholdEvent;
import com.terfehr.homehub.application.event.payload.CreateHouseholdEventPayload;
import com.terfehr.homehub.domain.shared.exception.InvalidHouseholdException;
import com.terfehr.homehub.domain.household.exception.InvalidRoommateException;
import com.terfehr.homehub.domain.household.exception.InvalidUserException;
import com.terfehr.homehub.domain.household.repository.HouseholdRepositoryInterface;
import com.terfehr.homehub.domain.household.repository.UserRepositoryInterface;
import com.terfehr.homehub.domain.shared.exception.InvalidNameException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Transactional
public class CreateHouseholdService {

    private final ApplicationEventPublisher publisher;
    private final AuthUserProviderInterface userProvider;
    private final UserRepositoryInterface userRepository;
    private final HouseholdRepositoryInterface householdRepository;

    /**
     * Executes the CreateHouseholdCommand by creating a new Household and adding the given user as the first admin.
     *
     * @param cmd The Command to execute.
     * @return A HouseholdDTO containing the newly created Household with id and name.
     * @throws AuthenticationCredentialsNotFoundException If there is no user in the security context.
     * @throws UserNotFoundException If the User to update does not exist.
     * @throws InvalidNameException If the given name is invalid.
     * @throws InvalidUserException If the given User to the HouseholdDTO is invalid.
     * @throws InvalidHouseholdException If the created Household for the Roommate is invalid.
     * @throws InvalidRoommateException If the created Roommate for the Household is invalid.
     */
    public HouseholdDTO execute(CreateHouseholdCommand cmd) throws AuthenticationCredentialsNotFoundException,
            UserNotFoundException,
            InvalidNameException,
            InvalidUserException,
            InvalidHouseholdException,
            InvalidRoommateException
    {
        User user = userProvider.getUser();

        Household household = new Household(cmd.name());
        Roommate roommate = new Roommate(household, user);
        household.addRoommate(roommate);
        user.addRoommate(roommate);

        householdRepository.save(household);
        userRepository.save(user);

        CreateHouseholdEventPayload payload = new CreateHouseholdEventPayload(household.getId(), household.getName());
        CreateHouseholdEvent event = new CreateHouseholdEvent(this, payload);
        publisher.publishEvent(event);

        return new HouseholdDTO(household.getId(), household.getName());
    }
}
