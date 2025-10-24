package com.terfehr.homehub.controller.request;

import com.terfehr.homehub.infrastructure.jackson.Trim;
import jakarta.validation.constraints.NotBlank;

public record DeleteUserRequest(
        @Trim
        @NotBlank(message = "Password cannot be blank!")
        String password
) {}
