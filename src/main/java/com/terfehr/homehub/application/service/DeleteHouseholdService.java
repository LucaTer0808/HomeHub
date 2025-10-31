package com.terfehr.homehub.application.service;

import com.terfehr.homehub.application.command.DeleteHouseholdCommand;
import com.terfehr.homehub.application.exception.HouseholdNotFoundException;
import com.terfehr.homehub.domain.household.entity.Household;
import com.terfehr.homehub.domain.household.entity.User;
import com.terfehr.homehub.domain.household.event.DeleteHouseholdEvent;
import com.terfehr.homehub.domain.household.event.payload.DeleteHouseholdEventPayload;
import com.terfehr.homehub.domain.household.exception.InvalidRoommateException;
import com.terfehr.homehub.domain.household.repository.HouseholdRepositoryInterface;
import com.terfehr.homehub.domain.household.repository.UserRepositoryInterface;
import com.terfehr.homehub.domain.household.service.UserService;
import com.terfehr.homehub.domain.shared.exception.InvalidDomainEventPayloadException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
@Transactional
public class DeleteHouseholdService {

    private final ApplicationEventPublisher publisher;
    private final HouseholdRepositoryInterface householdRepository;
    private final UserRepositoryInterface userRepository;
    private final UserService userService;

    /**
     * Executes the DeleteHouseholdCommand by fetching the represented Household from the Database and deleting it.
     * Due to cascadeType.ALL, all entities that belong to the Household will be deleted as well, including the Roommates and Invitations.
     *
     * @param cmd The Command to execute.
     * @throws HouseholdNotFoundException If the Household to delete does not exist.
     * @throws InvalidDomainEventPayloadException If the event payload is invalid.
     * @throws InvalidRoommateException If the roommate to delete from the User is invalid. Should not occur here.
     */
    public void execute(DeleteHouseholdCommand cmd) throws HouseholdNotFoundException, InvalidDomainEventPayloadException, InvalidRoommateException {

        Household household = householdRepository.findById(cmd.id())
                .orElseThrow(() -> new HouseholdNotFoundException("There is no household with id:  " + cmd.id()));

        updateUsers(household);
        householdRepository.delete(household);

        DeleteHouseholdEventPayload payload = new DeleteHouseholdEventPayload(cmd.id());
        DeleteHouseholdEvent event = new DeleteHouseholdEvent(this, payload);
        publisher.publishEvent(event);
    }

    /**
     * Updates the Users that are associated with the given Household via an Invitation or a Household.
     * It removes the Roommates as well as the Invitations from said Users.
     *
     * @param household The Household to get the Users for.
     */
    private void updateUsers(Household household) {
        Set<User> changedUsers = userService.removeRoommatesByHousehold(household);
        changedUsers.addAll(userService.removeInvitationsByHousehold(household));
        userRepository.saveAll(changedUsers);
    }
}
