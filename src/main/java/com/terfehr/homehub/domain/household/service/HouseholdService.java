package com.terfehr.homehub.domain.household.service;

import com.terfehr.homehub.domain.household.entity.*;
import com.terfehr.homehub.domain.shared.exception.InvalidInvitationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class HouseholdService {

    /**
     * Creates a new Roommate from the given Invitation and then removes the Invitation from the Household and the User.
     * Also adds the newly created Roommate to the Household and the User.
     *
     * @param invitation The Invitation to convert to a Roommate.
     * @return The newly created Roommate.
     */
    public Roommate convertInvitationToRoommate(Invitation invitation) {
        Household household = invitation.getHousehold();
        User user = invitation.getUser();

        household.removeInvitation(invitation);
        user.removeInvitation(invitation);

        Roommate roommate = new Roommate(household, user);

        household.addRoommate(roommate);
        user.addRoommate(roommate);

        return roommate;
    }

    /**
     * Deletes the given invitation from the household it is associated with.
     *
     * @param invitation The invitation to delete from households.
     * @throws InvalidInvitationException If the provided Invitation is invalid or not associated with this household. Should NOT happen here!
     */
    public void deleteInvitationFromHousehold(Invitation invitation) throws InvalidInvitationException {
        Household household = invitation.getHousehold();
        household.removeInvitation(invitation);
    }
    

    /**
     * Lets the given Roommate leave their respective Household.
     *
     * @param roommate The Roommate to leave the Household of.
     */
    public void leaveHousehold(Roommate roommate) {
        roommate.getHousehold().removeRoommate(roommate);
    }

    /**
     * Removes all invitations associated with the given household from the Invitations collection of the User
     * represented by the Invitation
     * @param household The household to remove the invitations from.
     * @throws InvalidInvitationException If the given household is invalid. Should not occur here.
     * @return The set of users whose invitations were removed.
     */
    public Set<User> removeInvitationsByHousehold(Household household) throws InvalidInvitationException {
        Set<User> changedUsers = new HashSet<>();

        for (Invitation invitation : household.getInvitations()) {
            User user = invitation.getUser();
            user.removeInvitation(invitation);
            changedUsers.add(user);
        }

        return changedUsers;
    }
}
