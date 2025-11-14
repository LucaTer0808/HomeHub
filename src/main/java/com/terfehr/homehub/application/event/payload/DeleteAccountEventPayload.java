package com.terfehr.homehub.application.event.payload;

import org.springframework.lang.NonNull;

public record DeleteAccountEventPayload(long id, @NonNull String name, @NonNull String balance) {
}
