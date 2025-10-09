package com.terfehr.homehub.application.command;

import lombok.Builder;
import org.springframework.lang.NonNull;

/**
 * Command for logging in a User with his email and password.
 *
 * @param email The email of the user
 * @param password The password of the user
 */
@Builder
public record UserLoginCommand(@NonNull String email, @NonNull String password) {}
