package com.terfehr.homehub.application.dto;

import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

public record RefreshVerificationCodeDTO(@NonNull UserDTO userDto, @NonNull String verificationCode, @NonNull LocalDateTime expiration) {
}
