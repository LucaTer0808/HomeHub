package com.terfehr.homehub.controller.response;

import com.terfehr.homehub.application.dto.UserDTO;
import com.terfehr.homehub.application.exception.InvalidResetPasswordDTOException;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ResetPasswordResponse {

    private final UserDTO dto;
    private final LocalDateTime timestamp;

    /**
     * Constructor for ResetPasswordResponse.
     *
     * @param dto The dto to set.
     * @throws InvalidResetPasswordDTOException If dto is null.d
     */
    public ResetPasswordResponse(UserDTO dto) throws InvalidResetPasswordDTOException {
        if (dto == null) {
            throw new InvalidResetPasswordDTOException("ResetPasswordDTO cannot be null");
        }
        this.dto = dto;
        this.timestamp = LocalDateTime.now();
    }
}
