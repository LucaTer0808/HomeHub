package com.terfehr.homehub.application.service;

import com.terfehr.homehub.application.command.InviteUserToHouseholdCommand;
import com.terfehr.homehub.application.dto.UserInvitationDTO;
import com.terfehr.homehub.application.exception.HouseholdNotFoundException;
import com.terfehr.homehub.application.exception.UserNotFoundException;
import com.terfehr.homehub.domain.household.entity.Household;
import com.terfehr.homehub.domain.household.entity.Invitation;
import com.terfehr.homehub.domain.household.entity.User;
import com.terfehr.homehub.application.event.InviteUserToHouseholdEvent;
import com.terfehr.homehub.application.event.payload.InviteUserToHouseholdEventPayload;
import com.terfehr.homehub.domain.shared.exception.InvalidHouseholdException;
import com.terfehr.homehub.domain.shared.exception.InvalidInvitationException;
import com.terfehr.homehub.domain.shared.exception.InvalidUserException;
import com.terfehr.homehub.domain.household.repository.HouseholdRepositoryInterface;
import com.terfehr.homehub.domain.household.repository.UserRepositoryInterface;
import com.terfehr.homehub.domain.shared.exception.InvalidEventPayloadException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Transactional
public class InviteUserToHouseholdService {

    private final ApplicationEventPublisher  publisher;
    private final HouseholdRepositoryInterface householdRepository;
    private final UserRepositoryInterface userRepository;

    /**
     * Executes the given InviteUserToHouseholdCommand by loading the represented User and Household from the Database,
     * creating an Invitation and then saving it in the Users and Households collection for Invitations. Additionally,
     * an Event informing about the Invitation is published.
     *
     * @param cmd The InviteUserToHouseholdCommand to execute containing Information about the ID of the
     *            Household and the ID of the invited User.
     * @return A UserInvitationDTO containing the ID of the Household and the Email of the User that was invited.
     * @throws HouseholdNotFoundException If the Household represented by the given ID does not exist.
     * @throws UserNotFoundException If the User with the given Email does not exist.
     * @throws InvalidHouseholdException If the Household is insufficient for creating an Invitation from it.
     * @throws InvalidUserException If the User is insufficient for creating an Invitation from or for it.
     * @throws InvalidInvitationException If the created Invitation is insufficient for adding it to the Users collection.
     * @throws InvalidEventPayloadException If the created EventPayload in invalid and can not function as payload for an DomainEvent.
     */
    public UserInvitationDTO execute(InviteUserToHouseholdCommand cmd) throws
            HouseholdNotFoundException,
            UserNotFoundException,
            InvalidHouseholdException,
            InvalidUserException,
            InvalidInvitationException,
            InvalidEventPayloadException
    {
        Household household = householdRepository.findById(cmd.id())
                .orElseThrow(() -> new HouseholdNotFoundException("There is no Household with he id: " + cmd.id()));

        User user = userRepository.findByEmail(cmd.email())
                .orElseThrow(() -> new UserNotFoundException("There is no User with the Email: " + cmd.email()));

        Invitation invitation = household.invite(user);
        user.receiveInvitation(invitation);

        householdRepository.save(household);
        userRepository.save(user);

        InviteUserToHouseholdEventPayload payload = new InviteUserToHouseholdEventPayload(household.getId(), user.getId());
        InviteUserToHouseholdEvent event = new InviteUserToHouseholdEvent(this, payload);
        publisher.publishEvent(event);

        return new UserInvitationDTO(household.getId(), user.getEmail());
    }
}
