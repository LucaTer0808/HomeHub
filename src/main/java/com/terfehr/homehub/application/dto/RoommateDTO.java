package com.terfehr.homehub.application.dto;

import org.springframework.lang.NonNull;

public record RoommateDTO(long householdId, @NonNull String email, @NonNull String role) {
}
