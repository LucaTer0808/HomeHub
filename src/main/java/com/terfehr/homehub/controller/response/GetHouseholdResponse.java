package com.terfehr.homehub.controller.response;

import com.terfehr.homehub.application.dto.HouseholdDTO;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

public record GetHouseholdResponse(@NonNull HouseholdDTO dto, @NonNull LocalDateTime timestamp) {
    public GetHouseholdResponse(HouseholdDTO dto) {
        this(dto, LocalDateTime.now());
    }
}
