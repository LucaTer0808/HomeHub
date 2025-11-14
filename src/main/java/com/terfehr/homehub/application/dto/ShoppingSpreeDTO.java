package com.terfehr.homehub.application.dto;

import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.Set;

public record ShoppingSpreeDTO(long shoppingSpreeId, @NonNull LocalDateTime date, @NonNull Set<ShoppingSpreeItemDTO> items, long shoppingExpenseId) {
}
