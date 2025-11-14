package com.terfehr.homehub.application.dto;

import org.springframework.lang.NonNull;

public record AccountDTO(long id, @NonNull String name, long balance, @NonNull String formattedBalance) {
}
