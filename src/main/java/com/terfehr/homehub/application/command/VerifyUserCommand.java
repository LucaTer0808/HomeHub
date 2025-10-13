package com.terfehr.homehub.application.command;

import lombok.Builder;
import org.springframework.lang.NonNull;

/**
 * Command for verifying a User. Contains the verification code sent to the User's email.
 * Each user has a 1:1 relationship with a verification code, which is unique.
 */
@Builder
public record VerifyUserCommand(@NonNull String verificationCode) {}
