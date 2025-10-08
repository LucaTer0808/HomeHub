package com.terfehr.homehub.controller.response;

import com.terfehr.homehub.application.dto.UserDTO;
import com.terfehr.homehub.application.exception.InvalidUserDTOException;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RegisterUserResponse {

    private final UserDTO user;
    private final LocalDateTime registeredAt;

    /**
     * Constructor for RegisterUserResponse.
     *
     * @param user The UserDTO of the just registered User.
     * @throws InvalidUserDTOException If the given UserDTO is invalid.
     */
    public RegisterUserResponse(UserDTO user) throws InvalidUserDTOException {
        if (!validateUser(user)) {
            throw new InvalidUserDTOException("Invalid user");
        }
        this.user = user;
        this.registeredAt = LocalDateTime.now();
    }

    /**
     * Validates the given UserDTO. It as well as his ID has to be not null.
     *
     * @param user The User to validate.
     * @return True if the User is valid, false otherwise.
     */
    private  boolean validateUser(UserDTO user) {
        return user != null;
    }
}
