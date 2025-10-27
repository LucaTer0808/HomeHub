package com.terfehr.homehub.controller.response;

import com.terfehr.homehub.application.dto.UserDTO;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

public record VerifyEmailChangeResponse(@NonNull UserDTO dto, @NonNull LocalDateTime timestamp) {
    public VerifyEmailChangeResponse(UserDTO dto) {
        this(dto, LocalDateTime.now());
    }
}
