package com.terfehr.homehub.domain.household.event.payload;

import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

public record ChangeEmailEventPayload(@NonNull Long id, @NonNull String email, @NonNull String pendingEmail, @NonNull String changeEmailVerificationCode, @NonNull LocalDateTime expiration) {
}
