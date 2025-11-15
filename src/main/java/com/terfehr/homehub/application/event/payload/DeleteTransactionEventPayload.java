package com.terfehr.homehub.application.event.payload;

import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

public record DeleteTransactionEventPayload(long id, long amount, @NonNull String description, @NonNull LocalDateTime date) {
}
