package com.terfehr.homehub.domain.household.event.payload;

public record InviteUserToHouseholdEventPayload(long householdId, long userId) {
}
