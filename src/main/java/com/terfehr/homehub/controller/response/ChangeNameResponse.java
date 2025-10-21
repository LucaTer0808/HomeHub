package com.terfehr.homehub.controller.response;

import com.terfehr.homehub.application.dto.UserDTO;
import com.terfehr.homehub.application.exception.InvalidUserDTOException;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChangeNameResponse {

    private final UserDTO dto;
    private final LocalDateTime timestamp;

    public ChangeNameResponse(UserDTO dto) {
        if (dto == null) {
            throw new InvalidUserDTOException("UserDTO cannot be null");
        }
        this.dto = dto;
        this.timestamp = LocalDateTime.now();
    }
}
