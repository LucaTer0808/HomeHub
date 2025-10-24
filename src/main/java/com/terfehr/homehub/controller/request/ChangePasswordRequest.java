package com.terfehr.homehub.controller.request;

import com.terfehr.homehub.infrastructure.jackson.Trim;
import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequest(
    @Trim
    @NotBlank(message = "Password cannot be blank!")
    String password,

    @Trim
    @NotBlank(message = "Confirmed password cannot be blank!")
    String confirmPassword
) {}
