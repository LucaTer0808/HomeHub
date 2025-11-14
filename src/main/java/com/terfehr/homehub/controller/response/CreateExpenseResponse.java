package com.terfehr.homehub.controller.response;

import com.terfehr.homehub.application.dto.ExpenseDTO;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

public record CreateExpenseResponse(@NonNull ExpenseDTO dto, @NonNull LocalDateTime timestamp) {
    public CreateExpenseResponse(ExpenseDTO dto) {
        this(dto, LocalDateTime.now());
    }
}
