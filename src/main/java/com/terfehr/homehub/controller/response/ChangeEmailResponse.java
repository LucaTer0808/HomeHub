package com.terfehr.homehub.controller.response;

import com.terfehr.homehub.application.dto.ChangeEmailDTO;
import com.terfehr.homehub.application.exception.InvalidChangeEmailDTOException;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChangeEmailResponse {

    private final ChangeEmailDTO dto;
    private final LocalDateTime timestamp;

    public ChangeEmailResponse(ChangeEmailDTO dto) {
        if (dto == null) {
            throw new InvalidChangeEmailDTOException("ChangeEmailDTO cannot be null");
        }
        this.dto = dto;
        this.timestamp = LocalDateTime.now();
    }

}
