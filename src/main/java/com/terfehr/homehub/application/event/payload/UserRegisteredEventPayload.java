package com.terfehr.homehub.application.event.payload;

public record UserRegisteredEventPayload(Long id, String email, String username){
}
