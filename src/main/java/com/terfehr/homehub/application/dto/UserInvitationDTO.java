package com.terfehr.homehub.application.dto;

import org.springframework.lang.NonNull;

public record UserInvitationDTO(long householdId, @NonNull String email) {
}
