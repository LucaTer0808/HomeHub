package com.terfehr.homehub.domain.household.key;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>The RoommateId class represents a composite primary key for identifying
 * the association between a user and a household in a database.</p>
 *
 * <p>This class is annotated with @Embeddable, indicating that it can be embedded
 * in an entity and used as a composite key. It implements Serializable to ensure
 * the composite key can be serialized and deserialized correctly.</p>
 *
 * <p>The RoommateId class consists of two identifiers:
 * <ul>
 * <li>userId: The ID of the user.</li>
 * <li>householdId: The ID of the household.</li>
 * </ul></p>
 *
 * <p>This class is equipped with a no-argument constructor and an all-argument
 * constructor, provided by the Lombok annotations @NoArgsConstructor and @AllArgsConstructor.</p>
 */
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RoommateId implements Serializable {

    private Long userId;
    private Long householdId;
}
