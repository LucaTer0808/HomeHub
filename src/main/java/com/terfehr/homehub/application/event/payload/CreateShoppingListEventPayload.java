package com.terfehr.homehub.application.event.payload;

import org.springframework.lang.NonNull;

public record CreateShoppingListEventPayload(long householdId, long shoppingListId, @NonNull String name) {
}
