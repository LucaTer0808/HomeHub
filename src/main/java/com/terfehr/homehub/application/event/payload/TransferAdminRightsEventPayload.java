package com.terfehr.homehub.application.event.payload;

import org.springframework.lang.NonNull;

public record TransferAdminRightsEventPayload(long id, @NonNull String email, @NonNull String role) {
}
