package com.terfehr.homehub.application.event.payload;

public record UserLoginEventPayload(Long id, String username, String email, boolean enabled) {
}
