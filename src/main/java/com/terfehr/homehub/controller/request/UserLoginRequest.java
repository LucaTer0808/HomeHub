package com.terfehr.homehub.controller.request;

import com.terfehr.homehub.infrastructure.jackson.Trim;
import com.terfehr.homehub.infrastructure.jackson.TrimAndLowerCase;
import jakarta.validation.constraints.NotBlank;

public record UserLoginRequest(
        @TrimAndLowerCase
        @NotBlank(message = "Email cannot be blank!")
        String email,

        @Trim
        @NotBlank(message = "Password cannot be blank!")
        String password
) {}
