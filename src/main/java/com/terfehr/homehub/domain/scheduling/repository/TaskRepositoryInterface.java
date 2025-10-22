package com.terfehr.homehub.domain.scheduling.repository;

import com.terfehr.homehub.domain.household.entity.Roommate;
import com.terfehr.homehub.domain.scheduling.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;
import java.util.Set;

public interface TaskRepositoryInterface extends JpaRepository<Task, Long> {

    /**
     * Retrieves a Task by its ID.
     *
     * @param id The ID of the Task.
     * @return An Optional containing either the Task or null if it does not exist.
     */
    @NonNull
    Optional<Task> findById(@NonNull Long id);

    /**
     * Retrieves all Tasks for a given Roommate.
     *
     * @param roommate The Roommate to get Tasks for.
     * @return A Set of Tasks.
     */
    Set<Task> findAllByRoommate(@NonNull Roommate roommate);
}
