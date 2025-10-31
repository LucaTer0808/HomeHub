package com.terfehr.homehub.controller.response;

import com.terfehr.homehub.application.dto.RoommateDTO;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

public record JoinHouseholdResponse(@NonNull RoommateDTO dto, @NonNull LocalDateTime timestamp) {
    public JoinHouseholdResponse(RoommateDTO dto) {
        this(dto, LocalDateTime.now());
    }
}
