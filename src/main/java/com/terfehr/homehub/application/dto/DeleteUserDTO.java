package com.terfehr.homehub.application.dto;

import org.springframework.lang.NonNull;

public record DeleteUserDTO(@NonNull Long id, @NonNull String email, @NonNull String username) {
}
