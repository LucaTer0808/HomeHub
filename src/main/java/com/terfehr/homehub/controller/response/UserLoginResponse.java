package com.terfehr.homehub.controller.response;

import com.terfehr.homehub.application.dto.UserLoginDTO;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

public record UserLoginResponse(@NonNull UserLoginDTO dto, @NonNull LocalDateTime timestamp) {
    public UserLoginResponse(UserLoginDTO dto) {
        this(dto, LocalDateTime.now());
    }
}
