package com.terfehr.homehub.application.command;

import lombok.Builder;
import org.springframework.lang.NonNull;

@Builder
public record CreateAccountCommand(long id, @NonNull String name, long amount, @NonNull String currencyCode) {
}
