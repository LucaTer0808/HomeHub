package com.terfehr.homehub.controller.request;

import com.terfehr.homehub.infrastructure.jackson.Trim;
import jakarta.validation.constraints.NotBlank;

public record CreateHouseholdRequest(
        @Trim
        @NotBlank(message = "Name cannot be blank!")
        String name
) {}
