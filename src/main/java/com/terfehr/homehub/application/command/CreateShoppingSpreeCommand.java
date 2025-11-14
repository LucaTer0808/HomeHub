package com.terfehr.homehub.application.command;

import lombok.Builder;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

@Builder
public record CreateShoppingSpreeCommand(
        long householdId,
        @NonNull LocalDateTime time,
        long shoppingListId,
        long accountId,
        long amount,
        @NonNull String description,
        @NonNull String recipient,
        long roommateId
) {}
