package com.terfehr.homehub.domain.household.service;

import com.terfehr.homehub.domain.household.entity.*;
import com.terfehr.homehub.domain.household.repository.HouseholdRepositoryInterface;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class HouseholdService {

    private final HouseholdRepositoryInterface householdRepository;

    /**
     * Deletes the given roommates from all households they are part of. If they are the last roommate in a household,
     * the household is deleted. If they are an admin, the role is transferred to the first roommate in the household.
     *
     * @param roommates The set of roommates to delete from households.
     */
    public void deleteRoommatesFromHouseholds(Set<Roommate> roommates) {
        Set<Household> changesHouseholds = new HashSet<>();
        Set<Household> deletedHouseholds = new HashSet<>();

        for (Roommate roommate : roommates) {

            Household household = roommate.getHousehold();

            if (household.isLastRoommate()) {
                household.removeRoommate(roommate);
                deletedHouseholds.add(household);
            } else if (roommate.isAdmin()) {
                household.removeRoommate(roommate);
                household.promoteRandomUserToAdmin();
                changesHouseholds.add(household);
            } else {
                household.removeRoommate(roommate);
                changesHouseholds.add(household);
            }
        }

        householdRepository.saveAll(changesHouseholds);
        householdRepository.deleteAll(deletedHouseholds);
    }
}
