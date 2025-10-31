package com.terfehr.homehub.application.command;

import lombok.Builder;
import org.springframework.lang.NonNull;

@Builder
public record DeleteInvitationCommand(long id, @NonNull String email) {
}
