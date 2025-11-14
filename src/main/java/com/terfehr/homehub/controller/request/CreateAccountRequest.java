package com.terfehr.homehub.controller.request;

import com.terfehr.homehub.infrastructure.jackson.Trim;
import com.terfehr.homehub.infrastructure.jackson.TrimAndLowerCase;
import jakarta.validation.constraints.NotBlank;

public record CreateAccountRequest(
        @Trim
        @NotBlank(message = "Name cannot be blank!")
        String name,

        @TrimAndLowerCase
        @NotBlank(message = "CurrencyCode cannot be blank!")
        String currencyCode,

        long amount
) {}
