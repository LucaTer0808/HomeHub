package com.terfehr.homehub.controller.response;

import com.terfehr.homehub.application.dto.RefreshVerificationCodeDTO;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

public record RefreshVerificationCodeResponse(@NonNull RefreshVerificationCodeDTO dto, @NonNull LocalDateTime timestamp) {
    public RefreshVerificationCodeResponse(RefreshVerificationCodeDTO dto) {
        this(dto, LocalDateTime.now());
    }
}
