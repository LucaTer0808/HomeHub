package com.terfehr.homehub.application.event.payload;

import org.springframework.lang.NonNull;

public record ChangeHouseholdNameEventPayload(@NonNull Long id, @NonNull String name) {
}
