package com.terfehr.homehub.application.event.payload;

import org.springframework.lang.NonNull;

public record DeleteHouseholdEventPayload(@NonNull Long id) {
}
