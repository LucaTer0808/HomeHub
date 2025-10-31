package com.terfehr.homehub.domain.scheduling.service;

import com.terfehr.homehub.domain.scheduling.entity.Task;
import com.terfehr.homehub.domain.scheduling.repository.TaskRepositoryInterface;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepositoryInterface taskRepository;

    /**
     * Remove the executing Roommate from every Task in the given set.
     *
     * @param tasks The set of Tasks to remove the Roommate from.
     */
    public void removeRoommates(Set<Task> tasks) {
        tasks.forEach(Task::removeRoommate);
    }
}
