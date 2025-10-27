package com.terfehr.homehub.controller.response;

import com.terfehr.homehub.application.dto.ChangeEmailDTO;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

public record ChangeEmailResponse(@NonNull ChangeEmailDTO dto, @NonNull LocalDateTime timestamp) {
    public ChangeEmailResponse(ChangeEmailDTO dto) {
        this(dto, LocalDateTime.now());
    }
}
