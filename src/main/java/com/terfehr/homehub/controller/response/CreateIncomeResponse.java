package com.terfehr.homehub.controller.response;

import com.terfehr.homehub.application.dto.IncomeDTO;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

public record CreateIncomeResponse(@NonNull IncomeDTO dto, @NonNull LocalDateTime timestamp) {
    public CreateIncomeResponse(IncomeDTO dto) {
        this(dto, LocalDateTime.now());
    }
}
