package com.terfehr.homehub.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

/**
 * Command for registering a User. Contains information about his desired username, email and password.
 */
@Builder
public record RegisterUserCommand(@NonNull String username, @NonNull String email, @NonNull String password) {}
