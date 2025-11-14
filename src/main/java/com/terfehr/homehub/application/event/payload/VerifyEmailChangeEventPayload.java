package com.terfehr.homehub.application.event.payload;

import org.springframework.lang.NonNull;

public record VerifyEmailChangeEventPayload(@NonNull Long id, @NonNull String email) {
}
