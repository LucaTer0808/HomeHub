package com.terfehr.homehub.domain.household.event.payload;

import org.springframework.lang.NonNull;

public record ChangeNameEventPayload(@NonNull Long id, @NonNull String firstName, @NonNull String lastName) {
}
