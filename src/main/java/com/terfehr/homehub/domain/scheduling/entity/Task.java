package com.terfehr.homehub.domain.scheduling.entity;

import com.terfehr.homehub.domain.household.entity.Roommate;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private TaskList taskList;
    @Column(nullable = false)
    private String action;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private boolean finished;
    private LocalDateTime finishedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    private Roommate roommate;

    /**
     * Constructor for a Task object. If the parameters do not satisfy the requirements, an exception is thrown.
     *
     * @param action The action to perform by the Roommate.
     * @param description A brief description of the Task.
     * @param taskList The TaskList this Task belongs to.
     * @throws IllegalArgumentException If the parameters do not satisfy the requirements.
     */
    public Task(String action, String description, TaskList taskList) throws IllegalArgumentException {
        if (!validate(action, description, taskList)) {
            throw new IllegalArgumentException("Invalid Task object");
        }
        this.action = action;
        this.description = description;
        this.taskList = taskList;
        this.roommate = null;
        this.finished = false;
        this.finishedAt = null;
    }

    /**
     * Sets the action of the Task. If the action is invalid or the Task is already finished, an exception is thrown.
     *
     * @param action The action to set.
     * @throws IllegalArgumentException If the given action is invalid or the Task is already finished.
     */
    public void setAction(String action) throws IllegalArgumentException {
        if (!validateAction(action) || this.finished) {
            throw new IllegalArgumentException("Invalid action or already finished task");
        }
        this.action = action;
    }

    /**
     * Sets the description of the Task. If the description is invalid or the Task is already finished, an exception is thrown.
     *
     * @param description The description to set.
     * @throws IllegalArgumentException If the given description is invalid or the Task is already finished.
     */
    public void setDescription(String description) throws IllegalArgumentException {
        if (!validateDescription(description) || this.finished) {
            throw new IllegalArgumentException("Invalid description or already finished task");
        }
    }

    /**
     * Finishes the current task and sets the timestamp of the finishing time.
     */
    public void finishTask() throws IllegalArgumentException {
        this.finished = true;
        this.finishedAt = LocalDateTime.now();
    }

    /**
     * Assigns the Task to the given Roommate. If the given Roommate is invalid or the task is finished already, an exception is thrown.
     *
     * @param roommate The Roommate to assign the Task to.
     * @throws IllegalArgumentException If the Roommate is invalid or the Task is already finished.
     */
    public void assignToRoommate(Roommate roommate) throws IllegalArgumentException {
        if (!validateRoommate(roommate) || this.finished) {
            throw new IllegalArgumentException("Invalid Roommate or already finished task");
        }
        this.roommate = roommate;
    }

    /**
     * Unassigns the Task from a Roommate. If the Task is finished already, an exception is thrown.
     *
     * @throws IllegalArgumentException If the Task is finished already.
     */
    public void unassignFromRoommate() throws IllegalArgumentException {
        if (this.finished) {
            throw new IllegalArgumentException("Task is already finished");
        }
        this.roommate = null;
    }

    /**
     * Validates all given parameters by orchestrating to the internal validation methods.
     *
     * @param action The action to validate.
     * @param description The description to validate.
     * @param taskList The TaskList to validate.
     * @return True, if all parameters are valid. False otherwise.
     */
    private boolean validate(String action, String description, TaskList taskList) {
        return validateAction(action) && validateDescription(description) && validateTaskList(taskList);
    }

    /**
     * Validates the given action. It has to be not null and not empty.
     *
     * @param action The action to verify.
     * @return True, if the action is valid. False otherwise.
     */
    private boolean validateAction(String action) {
        return action != null && !action.isBlank();
    }

    /**
     * Validates the given description. It has to be not null.
     *
     * @param description The description to verify.
     * @return True, if the description is valid. False otherwise.
     */
    private boolean validateDescription(String description) {
        return description != null;
    }

    /**
     * Validates the given TaskList. It has to be not null.
     *
     * @param taskList The TaskList to validate.
     * @return True, if the TaskList is valid. False otherwise.
     */
    private boolean validateTaskList(TaskList taskList) {
        return taskList != null;
    }

    /**
     * Validates the given Roommate. It has to be not null.
     *
     * @param roommate The Roommate to validate.
     * @return True, if the Roommate is valid. False otherwise.
     */
    private boolean validateRoommate(Roommate roommate) {
        return roommate != null;
    }
}
