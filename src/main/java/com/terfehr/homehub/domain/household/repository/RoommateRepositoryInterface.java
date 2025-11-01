package com.terfehr.homehub.domain.household.repository;

import com.terfehr.homehub.domain.household.entity.Roommate;
import com.terfehr.homehub.domain.household.key.RoommateId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoommateRepositoryInterface extends JpaRepository<Roommate, RoommateId> {

    /**
     * Retrieves a Roommate by its household ID and user email. Those two make up the composite key of the Roommate.
     *
     * @param householdId The ID of the household.
     * @param userEmail The email of the user.
     * @return An Optional containing either the Roommate or null if it does not exist.
     */
    Optional<Roommate> findByHouseholdIdAndUserEmail(Long householdId, String userEmail);
}
