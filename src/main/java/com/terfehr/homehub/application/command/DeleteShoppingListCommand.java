package com.terfehr.homehub.application.command;

import lombok.Builder;

@Builder
public record DeleteShoppingListCommand(long householdId, long shoppingListId) {
}
