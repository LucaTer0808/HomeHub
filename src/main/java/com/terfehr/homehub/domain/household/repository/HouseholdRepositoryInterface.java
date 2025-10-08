package com.terfehr.homehub.domain.household.repository;

import com.terfehr.homehub.domain.household.entity.Household;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface HouseholdRepositoryInterface extends JpaRepository<Household, Long> {

    /**
     * Retrieves a Household by its ID from the database.
     *
     * @param id The ID of the household.
     * @return An Optional containing the Household or null if it does not exist.
     */
    @NonNull
    Optional<Household> findById(@NonNull Long id);
}
