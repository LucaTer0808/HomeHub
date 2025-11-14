package com.terfehr.homehub.application.event.payload;

public record UserVerifiedEventPayload(Long id, String username, String email, boolean enabled) {
}
