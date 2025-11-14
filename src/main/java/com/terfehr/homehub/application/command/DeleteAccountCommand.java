package com.terfehr.homehub.application.command;

import lombok.Builder;

@Builder
public record DeleteAccountCommand(long householdId, long accountId) {
}
