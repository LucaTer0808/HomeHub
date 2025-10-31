package com.terfehr.homehub.domain.household.repository;

import com.terfehr.homehub.domain.household.entity.Invitation;
import com.terfehr.homehub.domain.household.key.InvitationId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvitationRepositoryInterface extends JpaRepository<Invitation, InvitationId> {

    /**
     * Retrieves and Invitation by its household ID and user email. Those two make up the composite key of the Invitation.
     *
     * @param household The ID of the household.
     * @param userEmail The email of the user.
     * @return An Optional containing either the Invitation or null if it does not exist.
     */
    Optional<Invitation> findByHouseholdIdAndUserEmail(long household, String userEmail);
}
