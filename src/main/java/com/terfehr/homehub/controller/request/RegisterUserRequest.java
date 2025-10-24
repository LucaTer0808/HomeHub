package com.terfehr.homehub.controller.request;

import com.terfehr.homehub.infrastructure.jackson.Trim;
import com.terfehr.homehub.infrastructure.jackson.TrimAndLowerCase;
import jakarta.security.auth.message.callback.PrivateKeyCallback;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Locale;

public record RegisterUserRequest(
    @Trim
    @NotBlank(message = "Username cannot be blank!")
    String username,

    @TrimAndLowerCase
    @NotBlank(message = "Email cannot be blank!")
    String email,

    @Trim
    @NotBlank(message = "Password cannot be blank!")
    String password,

    @Trim
    @NotBlank(message = "Confirmed password cannot be blank!")
    String confirmPassword,

    @Trim
    @NotBlank(message = "First name cannot be blank!")
    String firstName,

    @Trim
    @NotBlank(message = "Last name cannot be blank!")
    String lastName
) {}
