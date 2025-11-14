package com.terfehr.homehub.application.event.payload;

import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

public record CreateShoppingSpreeEventPayload(long id, @NonNull LocalDateTime date) {
}
