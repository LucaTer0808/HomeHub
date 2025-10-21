package com.terfehr.homehub.domain.household.event.payload;

import org.springframework.lang.NonNull;

public record ChangePasswordEventPayload(@NonNull Long id, @NonNull String username) {
}
