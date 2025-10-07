package com.terfehr.homehub.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class RegisterUserCommand {

    private String username;
    private String email;
    private String password;
}
