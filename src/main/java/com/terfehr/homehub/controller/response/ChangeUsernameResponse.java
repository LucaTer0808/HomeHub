package com.terfehr.homehub.controller.response;

import com.terfehr.homehub.application.dto.ChangeUsernameDTO;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

public record ChangeUsernameResponse(@NonNull ChangeUsernameDTO dto, @NonNull LocalDateTime timestamp) {
    public ChangeUsernameResponse(ChangeUsernameDTO dto) {
        this(dto, LocalDateTime.now());
    }
}
