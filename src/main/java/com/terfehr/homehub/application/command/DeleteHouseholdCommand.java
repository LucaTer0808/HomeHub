package com.terfehr.homehub.application.command;

import lombok.Builder;

@Builder
public record DeleteHouseholdCommand(long id) {
}
