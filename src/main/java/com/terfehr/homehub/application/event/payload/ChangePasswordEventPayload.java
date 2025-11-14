package com.terfehr.homehub.application.event.payload;

import org.springframework.lang.NonNull;

public record ChangePasswordEventPayload(@NonNull Long id, @NonNull String username) {
}
