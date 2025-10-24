package com.terfehr.homehub.controller.request;

import com.terfehr.homehub.infrastructure.jackson.Trim;
import jakarta.validation.constraints.NotBlank;

public record VerifyUserRequest(
        @Trim
        @NotBlank(message = "VerificationCode cannot be blank!")
        String verificationCode
) {}
