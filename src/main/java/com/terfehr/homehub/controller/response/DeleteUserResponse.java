package com.terfehr.homehub.controller.response;

import com.terfehr.homehub.application.dto.DeleteUserDTO;
import com.terfehr.homehub.application.exception.InvalidDeleteUserDTOException;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DeleteUserResponse {

    private final DeleteUserDTO dto;
    private final LocalDateTime timestamp;

    public DeleteUserResponse(DeleteUserDTO dto) throws InvalidDeleteUserDTOException {
        if (dto == null) {
            throw new InvalidDeleteUserDTOException("DeleteUserDTO cannot be null");
        }
        this.dto = dto;
        this.timestamp = LocalDateTime.now();
    }
}
