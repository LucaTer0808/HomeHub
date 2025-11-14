package com.terfehr.homehub.application.event.payload;

import org.springframework.lang.NonNull;

public record ChangeUsernameEventPayload(@NonNull Long id, @NonNull String username) {
}
