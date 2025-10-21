package com.terfehr.homehub.application.command;

import lombok.Builder;
import org.springframework.lang.NonNull;

/**
 * Command for registering a User. Contains information about his desired username, email and password.
 */
@Builder
public record RegisterUserCommand(@NonNull String username, @NonNull String email, @NonNull String password, @NonNull String firstName, @NonNull String lastName) {}
