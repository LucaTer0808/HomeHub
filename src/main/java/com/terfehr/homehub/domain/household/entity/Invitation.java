package com.terfehr.homehub.domain.household.entity;

import com.terfehr.homehub.domain.shared.exception.InvalidHouseholdException;
import com.terfehr.homehub.domain.household.exception.InvalidUserException;
import com.terfehr.homehub.domain.household.key.InvitationId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * <p>The Invitation class represents the association between a User and a Household
 * before the User joined the Household.
 * An Invitation links a user to a household and indicates their application for a membership for
 * that household. This class uses a composite primary key to maintain the relationship
 * between the user and the household.</p>
 *
 * <p>The Invitation entity is mapped using JPA annotations:
 * <ul>
 * <li>It uses an embedded ID as a composite key for the association.</li>
 * <li>It contains references to both the User and Household entities using @ManyToOne relationships.</li>
 * </ul>
 * </p>
 *
 * <p>Relationships:
 * <ul>
 * <li>An Invitation is associated with a specific Household.</li>
 * <li>An Invitation is also associated with a specific User.</li>
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
@Table(name = "invitations")
public class Invitation {

    @EmbeddedId
    private InvitationId id;

    @ManyToOne
    @MapsId("householdId")
    @JoinColumn(name = "household_id")
    private Household household;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    public Invitation(Household household, User user) throws  InvalidHouseholdException, InvalidUserException {
        if (!validateHousehold(household)) {
            throw new InvalidHouseholdException("The given household is invalid. It most likely is null");
        }
        if (!validateUser(user)) {
            throw new InvalidUserException("The given user is invalid. It most likely is null");
        }

        this.id = new InvitationId(household.getId(), user.getId());
        this.household = household;
        this.user = user;
    }

    /**
     * Validates the given Household by checking if it is not null.
     *
     * @param household The Household to check
     * @return True, if the Household is valid. False otherwise.
     */
    private boolean validateHousehold(Household household) {
        return household != null;
    }

    /**
     * Validates the given User by checking if it is not null.
     *
     * @param user The User to check
     * @return True, if the User is valid. False otherwise.
     */
    private boolean validateUser(User user) {
        return user != null;
    }

    /**
     * Validates the given Expiration by checking if it is not null.
     *
     * @param expiration The Expiration to check
     * @return True, if the Expiration is valid. False otherwise.
     */
    private boolean validateExpiration(LocalDateTime expiration) {
        return expiration != null;
    }
}
