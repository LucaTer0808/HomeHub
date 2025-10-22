package com.terfehr.homehub.application.event.payload;

import org.springframework.lang.NonNull;

public record DeleteUserEventPayload(@NonNull String email, @NonNull String username) {
}
