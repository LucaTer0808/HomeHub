package com.terfehr.homehub.application.dto;

import org.springframework.lang.NonNull;

public record HouseholdDTO(@NonNull Long id, @NonNull String name) {
}
