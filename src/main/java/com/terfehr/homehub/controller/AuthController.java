package com.terfehr.homehub.controller;

import com.terfehr.homehub.application.command.RegisterUserCommand;
import com.terfehr.homehub.application.dto.UserDTO;
import com.terfehr.homehub.application.service.RegisterUserService;
import com.terfehr.homehub.controller.request.RegisterUserRequest;
import com.terfehr.homehub.controller.response.RegisterUserResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final RegisterUserService registerUserService;

    @GetMapping
    public String welcome() {
        return "Welcome to HomeHub! To register, please call /auth/register with POST";
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponse> register(@RequestBody RegisterUserRequest request) {
        if (!request.validate()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Registration request sent at " + LocalDateTime.now() + ". The reason might be a faulty password, username or email address.");
        }

        RegisterUserCommand command = RegisterUserCommand.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();

        UserDTO registeredUser = registerUserService.execute(command);

        return ResponseEntity.status(HttpStatus.CREATED).body(new RegisterUserResponse(registeredUser));
    }
}
