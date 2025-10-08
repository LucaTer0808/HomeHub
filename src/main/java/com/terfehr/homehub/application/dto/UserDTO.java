package com.terfehr.homehub.application.dto;

import com.terfehr.homehub.domain.household.entity.User;
import lombok.Getter;

/**
 * DTO for transporting user data between application and controller layer. Contains information about the Users ID, username and email.
 */
@Getter
public class UserDTO {

    private final Long id;
    private final String username;
    private final String email;

    /**
     * Constructs a UserDTO from a User.
     *
     * @param user The user to create the DTO from.
     * @throws IllegalArgumentException If the given User is invalid.
     */
    public UserDTO(User user) throws IllegalArgumentException {
        if (!validate(user)) {
            throw new IllegalArgumentException("Invalid user");
        }
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
    }

    /**
     * Validates the given User. He and his ID both have to be not null.
     *
     * @param user The user to validate.
     * @return True, if the user is valid. False otherwise.
     */
    private boolean validate(User user) {
        return user != null && user.getId() != null;
    }
}
