package com.terfehr.homehub.domain.household.event.payload;

public record UserVerifiedEventPayload(Long id, String username, String email, boolean enabled) {
}
