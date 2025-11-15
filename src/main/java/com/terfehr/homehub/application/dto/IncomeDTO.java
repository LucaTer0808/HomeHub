package com.terfehr.homehub.application.dto;

import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

public record IncomeDTO(long id, long amount, @NonNull String formattedAmount, @NonNull String description, @NonNull LocalDateTime date, @NonNull String source) {
}
