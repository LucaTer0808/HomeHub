package com.terfehr.homehub.application.command;

import lombok.Builder;
import org.springframework.lang.NonNull;

@Builder
public record ChangeShoppingListNameCommand(long id, @NonNull String name) {
}
