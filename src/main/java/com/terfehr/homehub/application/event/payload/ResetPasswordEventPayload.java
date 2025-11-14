package com.terfehr.homehub.application.event.payload;

import org.springframework.lang.NonNull;

public record ResetPasswordEventPayload(@NonNull Long userId, @NonNull String email, @NonNull String password) {
}
