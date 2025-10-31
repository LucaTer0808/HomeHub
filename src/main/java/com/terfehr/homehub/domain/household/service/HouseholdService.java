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
     * Deletes the given invitations from all households they are associated with the invitation itself.
     *
     * @param invitations The set of invitations to delete from households.
     * @throws InvalidInvitationException If the provided Invitation is invalid or not associated with this household. Should NOT happen here!
     */
    public Set<Household> deleteInvitationFromHouseholds(Set<Invitation> invitations) throws InvalidInvitationException {
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
    public Set<Household> leaveHouseholdWithAdminTransfer(Set<AbstractMap.SimpleEntry<Roommate, Household>> tuples) {
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
     * Simply lets all Roommates leave the household. They thereby lose access to all data, files, calendars and more of that household.
     * This only works, however, if the given Roommate is not an Admin of the Household and the Household has at least two Roommates.
     *
     * @param roommates The set of roommates to leave the household for.
     * @return A Set of Households that have been affected by the operation.
     */
    public Set<Household> leaveHousehold(Set<Roommate> roommates) {
        return roommates.stream()
                .peek(roommate -> roommate.getHousehold().removeRoommate(roommate))
                .map(Roommate::getHousehold)
                .collect(Collectors.toSet());
    }
}
