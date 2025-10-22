package com.terfehr.homehub.domain.household.event.payload;

import org.springframework.lang.NonNull;

public record CreateHouseholdEventPayload(@NonNull Long id, @NonNull String name) {
}
