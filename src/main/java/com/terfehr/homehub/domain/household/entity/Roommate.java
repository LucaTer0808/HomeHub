package com.terfehr.homehub.domain.household.entity;

import com.terfehr.homehub.domain.household.key.RoommateId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <p>The Roommate class represents the association between a User and a Household.
 * A Roommate links a user to a household and indicates their membership within
 * that household. This class uses a composite primary key to maintain the relationship
 * between the user and the household.</p>
 *
 * <p>The Roommate entity is mapped using JPA annotations:
 * <ul>
 * <li>It uses an embedded ID as a composite key for the association.</li>
 * <li>It contains references to both the User and Household entities using @ManyToOne relationships.</li>
 * </ul>
 * </p>
 *
 * <p>Relationships:
 * <ul>
 * <li>A Roommate is associated with a specific Household.</li>
 * <li>A Roommate is also associated with a specific User.</li>
 * </ul>
 * </p>
 *
 * <p>Constructors and Validation:
 * <ul>
 * <li>The default no-argument constructor is provided for JPA compliance.</li>
 * <li>A constructor is available to create a Roommate instance by associating a specific
 *   User with a specific Household, after validating the inputs.</li>
 * </ul>
 * </p>
 */
@Entity
@NoArgsConstructor
@Getter
public class Roommate {

    @EmbeddedId
    private RoommateId id;

    @ManyToOne
    @MapsId("householdId")
    private Household household;

    @ManyToOne
    @MapsId("userId")
    private User user;

    /**
     * Constructs a new Roommate instance associating a user with a household.
     *
     * @param household the household that the roommate belongs to
     * @param user the user representing the roommate
     */
    public Roommate(Household household, User user) {
        if (!validate(household, user)) {
            throw new IllegalArgumentException("Invalid Roommate object");
        }
        this.household = household;
        this.user = user;
        this.id = new RoommateId(household.getId(), user.getId()); // could be unnecessary due to @MapsId
    }

    /**
     * Validates the provided household and user by ensuring they meet specific
     * validation criteria defined in their respective methods.
     *
     * @param household the household to validate
     * @param user the user to validate
     * @return true if both the household and user are valid, false otherwise
     */
    private boolean validate(Household household, User user) {
        return validateHousehold(household) && validateUser(user);
    }

    /**
     * Validates whether the provided household object is not null.
     *
     * @param household the Household object to be validated
     * @return true if the household is not null; false otherwise
     */
    private boolean validateHousehold(Household household) {
        return household != null;
    }

    /**
     * Validates whether the provided user object is not null.
     *
     * @param user the User object to be validated
     * @return true if the user is not null; false otherwise
     */
    private boolean validateUser(User user) {
        return user != null;
    }
}
