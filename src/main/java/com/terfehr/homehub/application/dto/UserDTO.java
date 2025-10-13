package com.terfehr.homehub.application.dto;

import org.springframework.lang.NonNull;

/**
 * DTO for transporting user data between application and controller layer. Contains information about the Users ID, username and email as well as his enabled status.
 */
public record UserDTO(@NonNull Long id, @NonNull String username, @NonNull String email, boolean enabled) {}
