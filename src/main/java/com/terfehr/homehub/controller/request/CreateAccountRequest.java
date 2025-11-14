package com.terfehr.homehub.controller.request;

import com.terfehr.homehub.infrastructure.jackson.Trim;
import com.terfehr.homehub.infrastructure.jackson.TrimAndLowerCase;
import org.springframework.lang.NonNull;

public record CreateAccountRequest(
        @Trim
        @NonNull
        String name,

        @TrimAndLowerCase
        @NonNull
        String currencyCode,

        long amount) {
}
