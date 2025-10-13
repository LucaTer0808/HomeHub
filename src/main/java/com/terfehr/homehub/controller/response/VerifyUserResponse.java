package com.terfehr.homehub.controller.response;

import com.terfehr.homehub.application.dto.UserDTO;
import com.terfehr.homehub.application.exception.InvalidUserDTOException;
import lombok.Getter;

@Getter
public class VerifyUserResponse {

    private final UserDTO user;

    public VerifyUserResponse(UserDTO user) {
        if (!validateUser(user)) {
            throw new InvalidUserDTOException("Invalid UserDTO passed to VerifyUserResponse");
        }
        this.user = user;
    }


    /**
     * Validates the given UserDTO. It cannot be null.
     *
     * @param user The UserDTO to validate.
     * @return True, if the UserDTO is not null. False otherwise.
     */
    private boolean validateUser(UserDTO user) {
        return user != null;
    }
}
