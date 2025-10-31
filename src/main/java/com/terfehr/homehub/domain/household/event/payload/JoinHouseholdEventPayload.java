package com.terfehr.homehub.domain.household.event.payload;

import org.springframework.lang.NonNull;

public record JoinHouseholdEventPayload(long id, @NonNull String email, @NonNull String role) {
}
