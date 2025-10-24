package com.terfehr.homehub.controller.request;

import com.terfehr.homehub.infrastructure.jackson.Trim;
import jakarta.validation.constraints.NotBlank;

public record ChangeUsernameRequest(
        @Trim
        @NotBlank(message = "Username cannot be blank!")
        String username
) {}
