package com.terfehr.homehub.controller.request;

import com.terfehr.homehub.infrastructure.jackson.Trim;
import jakarta.validation.constraints.NotBlank;

public record ChangeHouseholdNameRequest(
        @Trim
        @NotBlank(message = "Household name cannot be blank!")
        String name
) {}
