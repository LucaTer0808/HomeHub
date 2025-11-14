package com.terfehr.homehub.controller.request;

import com.terfehr.homehub.infrastructure.jackson.Trim;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record CreateShoppingSpreeRequest(
        @NotBlank
        LocalDateTime time,

        long shoppingListId,

        long accountId,

        long amount,

        @Trim
        @NotBlank
        String description,

        @Trim
        @NotBlank
        String recipient,

        long roommateId
) {}
