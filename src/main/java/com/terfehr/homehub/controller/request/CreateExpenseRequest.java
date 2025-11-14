package com.terfehr.homehub.controller.request;

import com.terfehr.homehub.infrastructure.jackson.Trim;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateExpenseRequest(
        long amount,

        @Trim
        @NotBlank
        String description,

        @NotNull
        LocalDateTime date,

        @Trim
        @NotBlank
        String recipient
) {}
