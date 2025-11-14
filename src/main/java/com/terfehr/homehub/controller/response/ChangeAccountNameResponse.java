package com.terfehr.homehub.controller.response;

import com.terfehr.homehub.application.dto.AccountDTO;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

public record ChangeAccountNameResponse(@NonNull AccountDTO dto, @NonNull LocalDateTime timestamp) {
    public ChangeAccountNameResponse(AccountDTO dto) {
        this(dto, LocalDateTime.now());
    }
}
