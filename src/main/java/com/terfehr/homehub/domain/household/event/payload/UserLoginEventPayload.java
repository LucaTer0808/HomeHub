package com.terfehr.homehub.domain.household.event.payload;

public record UserLoginEventPayload(Long id, String username, String email, boolean enabled) {
}
