package com.terfehr.homehub.domain.household.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
public class Household {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany(mappedBy = "householdId", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Roommate> roommates;

    /**
     * Constructs a new Household instance with the specified name and an initial user.
     * The initial user is added to the household as a roommate upon creation.
     *
     * @param name the name of the household
     * @param initialUser the initial user to be added as a roommate in the household
     */
    public Household(String name, User initialUser) {
        this.name = name;
        this.roommates = new HashSet<>();
        new Roommate(this, initialUser);
    }

    /**
     * Adds a roommate to the current household.
     *
     * @param roommate the roommate to be added to the household
     */
    public void addRoommate(Roommate roommate) {
        if (!validateRoommate(roommate)) {
            throw new IllegalArgumentException("Invalid Roommate for this Household");
        }
        this.roommates.add(roommate);
    }

    /**
     * Validates if the provided Roommate instance is associated with this Household.
     * The validation ensures that the Roommate object is not null and that its associated
     * Household matches the current Household instance.
     *
     * @param roommate the Roommate object to validate
     * @return true if the Roommate object is valid and belongs to this Household, false otherwise
     */
    private boolean validateRoommate(Roommate roommate) {
        return roommate != null && roommate.getHousehold().equals(this);
    }
}
