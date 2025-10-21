package com.terfehr.homehub.controller.response;

import com.terfehr.homehub.application.dto.ChangeUsernameDTO;
import com.terfehr.homehub.application.dto.UserDTO;
import com.terfehr.homehub.application.exception.InvalidUserDTOException;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChangeUsernameResponse {

    private final ChangeUsernameDTO dto;
    private final LocalDateTime timestamp;

    public ChangeUsernameResponse(ChangeUsernameDTO dto) throws InvalidUserDTOException {
        if (dto == null) {
            throw new InvalidUserDTOException("UserDTO cannot be null");
        }
        this.dto = dto;
        this.timestamp = LocalDateTime.now();
    }
}
