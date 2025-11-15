package com.terfehr.homehub.application.command;

import lombok.Builder;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

@Builder
public record CreateIncomeCommand(long accountId, long amount, @NonNull String description, @NonNull LocalDateTime date, @NonNull String source) {
}
