package com.terfehr.homehub.application.service;

import com.terfehr.homehub.application.command.DeleteInvitationCommand;
import com.terfehr.homehub.application.exception.HouseholdNotFoundException;
import com.terfehr.homehub.application.exception.InvitationNotFoundException;
import com.terfehr.homehub.application.exception.UserNotFoundException;
import com.terfehr.homehub.domain.household.entity.Household;
import com.terfehr.homehub.domain.household.entity.Invitation;
import com.terfehr.homehub.domain.household.entity.User;
import com.terfehr.homehub.domain.shared.exception.InvalidInvitationException;
import com.terfehr.homehub.domain.household.repository.HouseholdRepositoryInterface;
import com.terfehr.homehub.domain.household.repository.InvitationRepositoryInterface;
import com.terfehr.homehub.domain.household.repository.UserRepositoryInterface;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Transactional
public class DeleteInvitationService {

    private final HouseholdRepositoryInterface householdRepository;
    private final UserRepositoryInterface userRepository;
    private final InvitationRepositoryInterface invitationRepository;

    /**
     * Executes the DeleteInvitationCommand by fetching the represented Invitation from the Database and deleting it.
     * It also is removed from the Users and Households collections for Invitations.
     *
     * @param cmd The Command to execute. Contains the ID of the Household and the Email of the invited User
     * @throws HouseholdNotFoundException If the Household represented by the given ID does not exist.
     * @throws UserNotFoundException If the User with the given Email does not exist.
     * @throws InvitationNotFoundException If the Invitation to delete does not exist.
     * @throws InvalidInvitationException If the Invitation to delete is invalid. Should not occur here.
     */
    public void execute(DeleteInvitationCommand cmd) {
        Invitation invitation = invitationRepository.findByHouseholdIdAndUserEmail(cmd.id(), cmd.email())
                .orElseThrow(() -> new InvitationNotFoundException("There is no invitation from the Household with ID "
                        + cmd.id() + " inviting the User with Email " + cmd.email()));

        removeInvitationFromUser(invitation);
        removeInvitationFromHousehold(invitation);

        invitationRepository.delete(invitation);
    }

    /**
     * Removes the given Invitation from the User associated with it.
     *
     * @param invitation The Invitation to remove.
     * @throws InvalidInvitationException If the Invitation is not associated with a User. Should never happen because the User is always connected to the invitation here.
     */
    private void removeInvitationFromUser(Invitation invitation) {
        User user = invitation.getUser();
        user.removeInvitation(invitation);
        userRepository.save(user);
    }

    /**
     * Removes the given Invitation from the Household associated with it.
     *
     * @param invitation The Invitation to remove.
     * @throws InvalidInvitationException If the Invitation is not associated with a Household. Should never happen because the Household is always connected to the invitation here.
     */
    private void removeInvitationFromHousehold(Invitation invitation) {
        Household household = invitation.getHousehold();
        household.removeInvitation(invitation);
        householdRepository.save(household);
    }
}
