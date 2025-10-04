package com.terfehr.homehub.domain.scheduling.repository;

import com.terfehr.homehub.domain.scheduling.entity.TaskList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

interface TaskListRepositoryInterface extends JpaRepository<TaskList, Long> {

    /**
     * Retrieves a TaskList by its ID.
     *
     * @param id The ID of the TaskList.
     * @return An Optional containing either the TaskList or null if it does not exist.
     */
    @NonNull
    Optional<TaskList> findById(@NonNull Long id);
}
