package com.terfehr.homehub.application.command;

import lombok.Builder;

@Builder
public record GetHouseholdCommand(long id) {
}
