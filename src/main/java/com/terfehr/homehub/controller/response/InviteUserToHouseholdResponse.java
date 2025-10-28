package com.terfehr.homehub.controller.response;

import com.terfehr.homehub.application.dto.UserInvitationDTO;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

public record InviteUserToHouseholdResponse(@NonNull UserInvitationDTO dto, @NonNull LocalDateTime timestamp) {
    public InviteUserToHouseholdResponse(UserInvitationDTO dto) {
        this(dto, LocalDateTime.now());
    }
}
