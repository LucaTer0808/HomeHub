package com.terfehr.homehub.controller.response;

import com.terfehr.homehub.application.dto.ForgotPasswordDTO;
import com.terfehr.homehub.domain.household.exception.InvalidForgotPasswordDTOException;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ForgotPasswordResponse {

    private final ForgotPasswordDTO dto;
    private final LocalDateTime timestamp;

    public ForgotPasswordResponse(ForgotPasswordDTO dto) throws InvalidForgotPasswordDTOException{
        if (dto == null) {
            throw new InvalidForgotPasswordDTOException("ForgotPasswordDTO cannot be null");
        }
        this.dto = dto;
        this.timestamp = LocalDateTime.now();
    }
}
