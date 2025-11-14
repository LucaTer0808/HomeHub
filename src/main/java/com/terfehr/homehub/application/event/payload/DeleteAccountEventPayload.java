package com.terfehr.homehub.application.event.payload;

import org.springframework.lang.NonNull;

public record DeleteAccountEventPayload(long householdId, long accountId, @NonNull String name, @NonNull String balance) {
}
