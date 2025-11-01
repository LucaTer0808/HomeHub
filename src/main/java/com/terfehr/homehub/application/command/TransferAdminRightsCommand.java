package com.terfehr.homehub.application.command;

import lombok.Builder;
import org.springframework.lang.NonNull;

@Builder
public record TransferAdminRightsCommand(long id, @NonNull String email) {
}
