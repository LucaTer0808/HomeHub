package com.terfehr.homehub.domain.household.service;

import com.terfehr.homehub.domain.household.entity.*;
import com.terfehr.homehub.domain.household.exception.InvalidInvitationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.Set;
import java.util.stream.Collectors;

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
     * Deletes the given invitations from all households they are associated with the invitation itself.
     *
     * @param invitations The set of invitations to delete from households.
     * @throws InvalidInvitationException If the provided Invitation is invalid or not associated with this household. Should NOT happen here!
     */
    public Set<Household> deleteInvitationsFromHousehold(Set<Invitation> invitations) throws InvalidInvitationException {
        return invitations.stream()
                .peek(invitation -> invitation.getHousehold().removeInvitation(invitation))
                .map(Invitation::getHousehold)
                .collect(Collectors.toSet());
    }

    /**
     * Makes the given Roommates leave their respective Households and then promotes another random Roommate
     * to the Admin Roommate of said Household.
     *
     * @param tuples A Set containing tuples of Roommates and Households. The Roommate always has to have admin rights.
     * @return A Set of Households that have been affected by the operation.
     */
    public Set<Household> leaveHouseholdsWithAdminTransfer(Set<AbstractMap.SimpleEntry<Roommate, Household>> tuples) {
        for (AbstractMap.SimpleEntry<Roommate, Household> tuple : tuples) {
            Household household = tuple.getValue();
            household.removeRoommate(tuple.getKey());
            household.promoteRandomUserToAdmin();
        }

        return tuples.stream()
                .map(AbstractMap.SimpleEntry::getValue)
                .collect(Collectors.toSet());
    }

    /**
     * Makes the given Roommate leave their respective Household and then promotes another random Roommate to the Admin Roommate of said Household.
     *
     * @param roommate The Roommate to leave the household for.
     * @param household The Household to leave the Roommate from.
     */
    public void leaveHouseholdWithAdminTransfer(Roommate roommate, Household household) {
        household.removeRoommate(roommate);
        household.promoteRandomUserToAdmin();
    }

    /**
     * Simply lets all Roommates leave the household. They thereby lose access to all data, files, calendars and more of that household.
     * This only works, however, if the given Roommate is not an Admin of the Household and the Household has at least two Roommates.
     *
     * @param roommates The set of roommates to leave the household for.
     * @return A Set of Households that have been affected by the operation.
     */
    public Set<Household> leaveHouseholds(Set<Roommate> roommates) {
        return roommates.stream()
                .peek(roommate -> roommate.getHousehold().removeRoommate(roommate))
                .map(Roommate::getHousehold)
                .collect(Collectors.toSet());
    }

    /**
     * Lets the given Roommate leave its respective Household. This only should be called, when you are sure that the Roommate is neither
     * an Admin nor the last user of the Household.
     *
     * @param roommate The Roommate to leave the household for.
     */
    public void leaveHousehold(Roommate roommate) {
        roommate.getHousehold().removeRoommate(roommate);
    }

    /**
     * Transfers the admin status from the old admin to the new admin.
     *
     * @param oldAdmin The Roommate that currently has admin rights.
     * @param newAdmin The Roommate that should have admin rights.
     */
    public void transferAdminRights(Roommate oldAdmin, Roommate newAdmin) {
        oldAdmin.setRole(Role.ROLE_USER.getValue());
        newAdmin.setRole(Role.ROLE_ADMIN.getValue());
    }
}
