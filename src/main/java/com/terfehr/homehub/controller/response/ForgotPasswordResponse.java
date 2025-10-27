package com.terfehr.homehub.controller.response;

import com.terfehr.homehub.application.dto.ForgotPasswordDTO;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

public record ForgotPasswordResponse(@NonNull ForgotPasswordDTO dto, @NonNull LocalDateTime timestamp) {
    public ForgotPasswordResponse(ForgotPasswordDTO dto) {
        this(dto, LocalDateTime.now());
    }
}
