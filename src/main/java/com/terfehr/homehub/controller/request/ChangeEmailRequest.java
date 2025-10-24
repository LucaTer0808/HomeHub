package com.terfehr.homehub.controller.request;

import com.terfehr.homehub.infrastructure.jackson.TrimAndLowerCase;
import jakarta.validation.constraints.NotBlank;

public record ChangeEmailRequest(
        @TrimAndLowerCase
        @NotBlank(message = "Email cannot be blank!")
        String email
) {}
