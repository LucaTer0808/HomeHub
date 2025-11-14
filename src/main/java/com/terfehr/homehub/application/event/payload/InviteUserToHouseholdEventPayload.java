package com.terfehr.homehub.application.event.payload;

public record InviteUserToHouseholdEventPayload(long householdId, long userId) {
}
