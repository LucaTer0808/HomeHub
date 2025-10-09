package com.terfehr.homehub.application.dto;

import org.springframework.lang.NonNull;

import java.util.Date;

/**
 * DTO for transporting user data between application and controller layer. Contains information about the Users ID, username and email as well as the JWT token
 * for authentication purposes. None of the fields can be null.
 */
public record UserLoginDTO(@NonNull UserDTO user, @NonNull String jwtToken, @NonNull Date issuedAt, @NonNull Date expiresAt) {}
