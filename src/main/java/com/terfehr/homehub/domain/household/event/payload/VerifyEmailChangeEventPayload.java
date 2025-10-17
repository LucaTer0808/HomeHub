package com.terfehr.homehub.domain.household.event.payload;

import org.springframework.lang.NonNull;

public record VerifyEmailChangeEventPayload(@NonNull Long id, @NonNull String email) {
}
