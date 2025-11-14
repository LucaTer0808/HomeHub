package com.terfehr.homehub.controller.response;

import com.terfehr.homehub.application.dto.ShoppingListDTO;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

public record CreateShoppingListResponse(@NonNull ShoppingListDTO dto, @NonNull LocalDateTime timestamp) {
    public CreateShoppingListResponse(ShoppingListDTO dto) {
        this(dto, LocalDateTime.now());
    }
}
