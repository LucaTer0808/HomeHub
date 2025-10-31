package com.terfehr.homehub.application.service;

import com.terfehr.homehub.application.command.JoinHouseholdCommand;
import com.terfehr.homehub.application.dto.RoommateDTO;
import com.terfehr.homehub.application.interfaces.AuthUserProviderInterface;
import com.terfehr.homehub.domain.household.entity.Household;
import com.terfehr.homehub.domain.household.entity.Invitation;
import com.terfehr.homehub.domain.household.entity.Roommate;
import com.terfehr.homehub.domain.household.entity.User;
import com.terfehr.homehub.domain.household.event.JoinHouseholdEvent;
import com.terfehr.homehub.domain.household.event.payload.JoinHouseholdEventPayload;
import com.terfehr.homehub.domain.household.repository.HouseholdRepositoryInterface;
import com.terfehr.homehub.domain.household.repository.InvitationRepositoryInterface;
import com.terfehr.homehub.domain.household.repository.UserRepositoryInterface;
import com.terfehr.homehub.domain.household.service.HouseholdService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JoinHouseholdService {

    private final ApplicationEventPublisher publisher;
    private final AuthUserProviderInterface userProvider;
    private final HouseholdRepositoryInterface householdRepository;
    private final HouseholdService householdService;
    private final InvitationRepositoryInterface invitationRepository;
    private final UserRepositoryInterface userRepository;

    public RoommateDTO execute(JoinHouseholdCommand cmd) {
        User user = userProvider.getUser();

        Invitation invitation = invitationRepository.findByHouseholdIdAndUserEmail(cmd.id(), user.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("There is no invitation from the Household with ID "
                        + cmd.id() + " inviting the User with Email " + user.getEmail()));

        Household household = invitation.getHousehold();
        Roommate roommate = householdService.convertInvitationToRoommate(invitation);

        userRepository.save(user);
        householdRepository.save(household);
        invitationRepository.delete(invitation);

        JoinHouseholdEventPayload payload = new JoinHouseholdEventPayload(household.getId(), user.getEmail(), roommate.getRole().getValue());
        JoinHouseholdEvent event = new JoinHouseholdEvent(this, payload);
        publisher.publishEvent(event);

        return new RoommateDTO(household.getId(), user.getEmail(), roommate.getRole().getValue());
    }
}
