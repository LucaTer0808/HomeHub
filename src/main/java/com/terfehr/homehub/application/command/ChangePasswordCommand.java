package com.terfehr.homehub.application.command;

import lombok.Builder;
import org.springframework.lang.NonNull;

@Builder
public record ChangePasswordCommand(@NonNull String password, @NonNull String confirmPassword) {
}
