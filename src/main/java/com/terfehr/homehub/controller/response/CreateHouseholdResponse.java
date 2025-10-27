package com.terfehr.homehub.controller.response;

import com.terfehr.homehub.application.dto.HouseholdDTO;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

public record CreateHouseholdResponse(@NonNull HouseholdDTO dto, @NonNull LocalDateTime timestamp) {
    public CreateHouseholdResponse(HouseholdDTO dto) {
        this(dto, LocalDateTime.now());
    }
}
