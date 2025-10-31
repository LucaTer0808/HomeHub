package com.terfehr.homehub.controller.request;

import com.terfehr.homehub.infrastructure.jackson.TrimAndLowerCase;
import jakarta.validation.constraints.NotBlank;

public record DeleteInvitationRequest(
        @TrimAndLowerCase
        @NotBlank
        String email
) {}
