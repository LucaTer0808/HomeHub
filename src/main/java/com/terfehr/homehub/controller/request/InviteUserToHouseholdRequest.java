package com.terfehr.homehub.controller.request;

import com.terfehr.homehub.infrastructure.jackson.TrimAndLowerCase;
import jakarta.validation.constraints.NotNull;

public record InviteUserToHouseholdRequest(
         @TrimAndLowerCase
         @NotNull
         String email
) {}
