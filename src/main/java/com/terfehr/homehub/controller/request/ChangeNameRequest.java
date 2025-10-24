package com.terfehr.homehub.controller.request;

import com.terfehr.homehub.infrastructure.jackson.Trim;
import jakarta.validation.constraints.NotBlank;

public record ChangeNameRequest(
    @Trim
    @NotBlank(message = "First name cannot be blank!")
    String firstName,

    @Trim
    @NotBlank(message = "Last name cannot be blank!")
    String lastName
) {}
