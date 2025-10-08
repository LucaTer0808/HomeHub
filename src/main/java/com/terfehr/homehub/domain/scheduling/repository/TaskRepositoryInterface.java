package com.terfehr.homehub.domain.scheduling.repository;

import com.terfehr.homehub.domain.scheduling.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

interface TaskRepositoryInterface extends JpaRepository<Task, Long> {

    /**
     * Retrieves a Task by its ID.
     *
     * @param id The ID of the Task.
     * @return An Optional containing either the Task or null if it does not exist.
     */
    @NonNull
    Optional<Task> findById(@NonNull Long id);
}
