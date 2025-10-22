package com.terfehr.homehub.domain.household.event.payload;

import org.springframework.lang.NonNull;

public record DeleteHouseholdEventPayload(@NonNull Long id) {
}
