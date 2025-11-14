package com.terfehr.homehub.application.dto;

import org.springframework.lang.NonNull;

public record ShoppingSpreeItemDTO(@NonNull String name, int quantity) {
}
