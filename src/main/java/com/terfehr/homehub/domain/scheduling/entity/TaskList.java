package com.terfehr.homehub.domain.scheduling.entity;

import com.terfehr.homehub.domain.household.entity.Household;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
public class TaskList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany(mappedBy = "taskList", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Task> tasks;
    @ManyToOne(fetch = FetchType.LAZY)
    private Household household;

    /**
     * Constructor for a new TaskList.
     *
     * @param name The name of the TaskList
     * @param household The Household the list belongs to
     * @throws IllegalArgumentException If the given parameters are invalid
     */
    public TaskList(String name, Household household) throws IllegalArgumentException {
        if (!validate(name, household)) {
            throw new IllegalArgumentException("Invalid TaskList object");
        }
        this.name = name;
        this.household = household;
        this.tasks = new HashSet<>();
    }

    /**
     * Sets the name of the TaskList. If the name is invalid, an exception is thrown.
     *
     * @param name The desired name for the TaskList.
     * @throws IllegalArgumentException If the given name is invalid.
     */
    public void setName(String name) throws IllegalArgumentException {
        if (!validateName(name)) {
            throw new IllegalArgumentException("Invalid TaskList object");
        }
        this.name = name;
    }

    /**
     * Adds a Task to the TaskList by building it on the fly by the parameters passed in the argument.
     *
     * @param action The action to perform in the task.
     * @param description The brief description of the task.
     * @throws IllegalArgumentException If the given parameters for the task are invalid.
     */
    public void addTask(String action, String description) throws IllegalArgumentException {
        this.tasks.add(new Task(action, description, this));
    }

    /**
     * Removes the given Task from the TaskList. If the Task is not contained by the TaskList, an exception is thrown.
     *
     * @param task The task to remove from the TaskList.
     * @throws IllegalArgumentException If the Task is not contained in the TaskList.
     */
    public void removeTask(Task task) throws IllegalArgumentException {
        if (!this.tasks.contains(task)) {
            throw new IllegalArgumentException("TaskList does not contain the given task");
        }
        this.tasks.remove(task);
    }

    /**
     * Validates the given parameters by calling and combining the return value
     * of the sub validation methods for name and household.
     *
     * @param name The desired name of the TaskList.
     * @param household The creating Household of the TaskList.
     * @return True, if all parameters are valid. False otherwise.
     */
    private boolean validate(String name, Household household) {
        return validateName(name) && validateHousehold(household);
    }

    /**
     * Validates the given name. It has to be not null and not empty.
     *
     * @param name The desired name.
     * @return True, if the name is valid. False otherwise
     */
    private boolean validateName(String name) {
        return name != null && !name.isBlank();
    }

    /**
     * Validates the given Household. It has to be not null.
     *
     * @param household The household to check.
     * @return True, if the given Household is valid. False otherwise.
     */
    private boolean validateHousehold(Household household) {
        return household != null;
    }
}
