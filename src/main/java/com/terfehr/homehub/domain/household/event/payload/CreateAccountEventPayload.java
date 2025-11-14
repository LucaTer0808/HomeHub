package com.terfehr.homehub.domain.household.event.payload;

import org.springframework.lang.NonNull;

public record CreateAccountEventPayload(long id, @NonNull String name, @NonNull String balance) {
}
