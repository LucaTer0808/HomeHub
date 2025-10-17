package com.terfehr.homehub.application.dto;

import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

public record ChangeEmailDTO(@NonNull UserDTO user, @NonNull String newEmail, @NonNull LocalDateTime expiration) {}
