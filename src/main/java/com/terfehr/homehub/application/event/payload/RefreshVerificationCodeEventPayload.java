package com.terfehr.homehub.application.event.payload;

import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

public record RefreshVerificationCodeEventPayload(@NonNull Long id, @NonNull String email, @NonNull String verificationCode, @NonNull LocalDateTime expiration) {
}
