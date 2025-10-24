package com.terfehr.homehub.controller.request;

import com.terfehr.homehub.infrastructure.jackson.Trim;
import jakarta.validation.constraints.NotBlank;

public record VerifyEmailChangeRequest(
        @Trim
        @NotBlank(message = "EmailChangeCode cannot be null")
        String emailChangeCode
) {}
