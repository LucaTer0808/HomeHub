package com.terfehr.homehub.controller;

import com.terfehr.homehub.application.command.RegisterUserCommand;
import com.terfehr.homehub.application.command.UserLoginCommand;
import com.terfehr.homehub.application.command.VerifyUserCommand;
import com.terfehr.homehub.application.dto.UserDTO;
import com.terfehr.homehub.application.dto.UserLoginDTO;
import com.terfehr.homehub.application.service.RegisterUserService;
import com.terfehr.homehub.application.service.UserLoginService;
import com.terfehr.homehub.application.service.VerifyUserService;
import com.terfehr.homehub.controller.request.RegisterUserRequest;
import com.terfehr.homehub.controller.request.UserLoginRequest;
import com.terfehr.homehub.controller.request.VerifyUserRequest;
import com.terfehr.homehub.controller.response.RegisterUserResponse;
import com.terfehr.homehub.controller.response.UserLoginResponse;
import com.terfehr.homehub.controller.response.VerifyUserResponse;
import com.terfehr.homehub.infrastructure.service.JwtService;
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
    private final UserLoginService userLoginService;
    private final VerifyUserService verifyUserService;

    @GetMapping
    public String welcome() {
        return "Welcome to HomeHub! To register, please call /auth/register with POST";
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponse> register(@RequestBody RegisterUserRequest request) {
        if (!request.validate()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Registration request sent at " + LocalDateTime.now() + ". The reason might be a faulty password, username or email address.");
        }

        RegisterUserCommand cmd = RegisterUserCommand
                .builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();

        UserDTO registeredUser = registerUserService.execute(cmd);

        return ResponseEntity.status(HttpStatus.CREATED).body(new RegisterUserResponse(registeredUser));
    }

    @PatchMapping("/verify")
    public ResponseEntity<VerifyUserResponse> verify(@RequestBody VerifyUserRequest request) {
        if (!request.validate()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid verification request sent at " + LocalDateTime.now());
        }

        VerifyUserCommand cmd = VerifyUserCommand
                .builder()
                .verificationCode(request.getVerificationCode())
                .build();

        UserDTO verifiedUser = verifyUserService.execute(cmd);
        return ResponseEntity.status(HttpStatus.OK).body(new VerifyUserResponse(verifiedUser));
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        if (!request.validate()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid login request sent at " + LocalDateTime.now());
        }

        UserLoginCommand cmd = UserLoginCommand
                .builder()
                .email(request.getEmail())
                .password((request.getPassword()))
                .build();

        UserLoginDTO dto = userLoginService.execute(cmd);
        return ResponseEntity.status(HttpStatus.OK).body(new UserLoginResponse(dto));
    }
}
