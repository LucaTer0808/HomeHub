package com.terfehr.homehub.controller.request;

import com.terfehr.homehub.infrastructure.jackson.Trim;
import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequest(
    @Trim
    @NotBlank(message = "Password cannot be blank!")
    String password,

    @Trim
    @NotBlank(message = "Confirmed password cannot be blank!")
    String confirmPassword,

    @Trim
    @NotBlank(message = "ForgotPasswordCode cannot be blank!")
    String forgotPasswordCode
) {}

