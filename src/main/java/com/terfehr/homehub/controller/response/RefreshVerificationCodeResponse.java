package com.terfehr.homehub.controller.response;

import com.terfehr.homehub.application.dto.RefreshVerificationCodeDTO;
import com.terfehr.homehub.application.exception.InvalidRefreshVerificationCodeDTOException;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RefreshVerificationCodeResponse {

    private final RefreshVerificationCodeDTO dto;
    private final LocalDateTime timestamp;

    public RefreshVerificationCodeResponse(RefreshVerificationCodeDTO dto) {
        if (dto == null) {
            throw new InvalidRefreshVerificationCodeDTOException("RefreshVerificationCodeDTO cannot be null");
        }

        this.dto = dto;
        this.timestamp = LocalDateTime.now();
    }

}
