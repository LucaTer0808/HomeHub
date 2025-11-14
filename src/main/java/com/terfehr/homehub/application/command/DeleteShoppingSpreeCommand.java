package com.terfehr.homehub.application.command;

import lombok.Builder;

@Builder
public record DeleteShoppingSpreeCommand(long id) {
}
