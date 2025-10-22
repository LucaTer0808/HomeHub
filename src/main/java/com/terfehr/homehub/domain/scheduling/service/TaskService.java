package com.terfehr.homehub.domain.scheduling.service;

import com.terfehr.homehub.domain.household.entity.Roommate;
import com.terfehr.homehub.domain.scheduling.entity.Task;
import com.terfehr.homehub.domain.scheduling.repository.TaskRepositoryInterface;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepositoryInterface taskRepository;

    /**
     * Fetches all Tasks that belong to the given Roommates and sets the reference to the Roommate to null.
     * @param roommates The Roommates to remove from all Tasks.
     */
    public void setNullByRoommates(Set<Roommate> roommates) {
        Set<Task> alteredTasks = roommates.stream()
                .flatMap(roommate -> taskRepository.findAllByRoommate(roommate).stream())
                .peek(Task::removeRoommate)
                .collect(Collectors.toSet());

        taskRepository.saveAll(alteredTasks);
    }
}
