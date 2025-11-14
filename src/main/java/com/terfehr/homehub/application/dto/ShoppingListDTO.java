package com.terfehr.homehub.application.dto;

import org.springframework.lang.NonNull;

public record ShoppingListDTO(long id, @NonNull String name) {
}
