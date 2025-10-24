package com.terfehr.homehub.application.command;

import lombok.Builder;
import org.springframework.lang.NonNull;

@Builder
public record RegisterUserCommand(@NonNull String username, @NonNull String email, @NonNull String password, @NonNull String confirmPassword, @NonNull String firstName, @NonNull String lastName) {}
