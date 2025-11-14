package com.terfehr.homehub.application.command;

import lombok.Builder;
import org.springframework.lang.NonNull;

@Builder
public record ChangeAccountNameCommand(long id, @NonNull String name) {
}
