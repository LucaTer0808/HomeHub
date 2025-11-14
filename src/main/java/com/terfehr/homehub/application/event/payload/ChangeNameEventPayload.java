package com.terfehr.homehub.application.event.payload;

import org.springframework.lang.NonNull;

public record ChangeNameEventPayload(@NonNull Long id, @NonNull String firstName, @NonNull String lastName) {
}
