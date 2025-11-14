package com.terfehr.homehub.application.event.payload;

import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

public record DeleteShoppingSpreeEventPayload(long id, @NonNull LocalDateTime date) {
}
