package com.terfehr.homehub.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Command for registering a User. Contains information about his desired username, email and password.
 */
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class RegisterUserCommand {

    private String username;
    private String email;
    private String password;
}
