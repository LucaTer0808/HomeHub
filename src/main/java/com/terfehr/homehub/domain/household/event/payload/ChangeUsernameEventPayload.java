package com.terfehr.homehub.domain.household.event.payload;

import org.springframework.lang.NonNull;

public record ChangeUsernameEventPayload(@NonNull Long id, @NonNull String username) {
}
