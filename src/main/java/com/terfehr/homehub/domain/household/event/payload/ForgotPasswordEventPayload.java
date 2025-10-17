package com.terfehr.homehub.domain.household.event.payload;

import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

public record ForgotPasswordEventPayload(@NonNull Long id, @NonNull String email, @NonNull String verificationCode, @NonNull LocalDateTime expiration) {
}
