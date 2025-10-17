package com.terfehr.homehub.application.dto;

import org.springframework.lang.NonNull;

public record ForgotPasswordDTO(@NonNull String email) {
}
