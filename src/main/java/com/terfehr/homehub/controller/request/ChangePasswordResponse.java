package com.terfehr.homehub.controller.request;

import com.terfehr.homehub.application.dto.UserDTO;
import com.terfehr.homehub.application.exception.InvalidUserDTOException;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChangePasswordResponse {

    private final UserDTO dto;
    private final LocalDateTime timestamp;

    public ChangePasswordResponse(UserDTO dto) throws InvalidUserDTOException {
        if (dto == null) {
            throw new InvalidUserDTOException("UserDTO cannot be null");
        }
        this.dto = dto;
        this.timestamp = LocalDateTime.now();
    }
}
