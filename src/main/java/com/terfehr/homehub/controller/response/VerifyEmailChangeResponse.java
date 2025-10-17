package com.terfehr.homehub.controller.response;

import com.terfehr.homehub.application.dto.UserDTO;
import com.terfehr.homehub.domain.household.exception.InvalidVerifyEmailChangeDTO;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class VerifyEmailChangeResponse {

    private final UserDTO dto;
    private final LocalDateTime timestamp;

    public VerifyEmailChangeResponse(UserDTO dto) throws InvalidVerifyEmailChangeDTO {
        if (dto == null) {
            throw new InvalidVerifyEmailChangeDTO("VerifyEmailChangeDTO cannot be null");
        }
        this.dto = dto;
        this.timestamp = LocalDateTime.now();
    }
}
