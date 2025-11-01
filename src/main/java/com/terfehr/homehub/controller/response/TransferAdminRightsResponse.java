package com.terfehr.homehub.controller.response;

import com.terfehr.homehub.application.dto.RoommateDTO;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

public record TransferAdminRightsResponse(@NonNull RoommateDTO dto, @NonNull LocalDateTime timestamp) {
    public TransferAdminRightsResponse(RoommateDTO dto) {
        this(dto, LocalDateTime.now());
    }
}
