package com.terfehr.homehub.controller.response;

import com.terfehr.homehub.application.dto.ShoppingSpreeDTO;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

public record CreateShoppingSpreeResponse(@NonNull ShoppingSpreeDTO shoppingSpreeDTO, @NonNull LocalDateTime timestamp) {
    public CreateShoppingSpreeResponse(ShoppingSpreeDTO shoppingSpreeDTO) {
        this(shoppingSpreeDTO, LocalDateTime.now());
    }
}
