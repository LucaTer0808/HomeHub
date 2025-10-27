package com.terfehr.homehub.controller.response;

import com.terfehr.homehub.application.dto.HouseholdDTO;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

public record ChangeHouseholdNameResponse(@NonNull HouseholdDTO dto, @NonNull LocalDateTime timestamp) {
    public ChangeHouseholdNameResponse(HouseholdDTO dto) {
        this(dto, LocalDateTime.now());
    }
}
