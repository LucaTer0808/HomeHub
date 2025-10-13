package com.terfehr.homehub.controller.response;

import com.terfehr.homehub.application.dto.UserLoginDTO;
import com.terfehr.homehub.application.exception.InvalidUserDTOException;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserLoginResponse {

    private final UserLoginDTO userLoginDTO;
    private final LocalDateTime timestamp;

    public UserLoginResponse(UserLoginDTO userLoginDTO) {
        if (!validateUserLoginDTO(userLoginDTO)) {
            throw new InvalidUserDTOException("Invalid token or UserDTO");
        }
        this.userLoginDTO = userLoginDTO;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Validates the given UserLoginDTO. It cannot be null.
     *
     * @param userLoginDTO The UserLoginDTO to validate.
     * @return True, if the UserLoginDTO is not null. False otherwise.
     */
    private boolean validateUserLoginDTO(UserLoginDTO userLoginDTO) {
        return userLoginDTO != null;
    }
}
