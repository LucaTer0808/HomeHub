package com.terfehr.homehub.application.dto;

import org.springframework.lang.NonNull;

import java.util.Date;

public record ChangeUsernameDTO(@NonNull UserDTO user, @NonNull String jwtToken, @NonNull Date createdAt, @NonNull Date expiresAt) {
}
