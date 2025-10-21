package com.terfehr.homehub.controller;

import com.terfehr.homehub.application.command.*;
import com.terfehr.homehub.application.dto.ForgotPasswordDTO;
import com.terfehr.homehub.application.dto.RefreshVerificationCodeDTO;
import com.terfehr.homehub.application.dto.UserDTO;
import com.terfehr.homehub.application.dto.UserLoginDTO;
import com.terfehr.homehub.application.service.*;
import com.terfehr.homehub.controller.request.*;
import com.terfehr.homehub.controller.response.*;
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

    private final ChangeEmailService changeEmailService;
    private final ForgotPasswordService forgotPasswordService;
    private final RegisterUserService registerUserService;
    private final ResetPasswordService resetPasswordService;
    private final UserLoginService userLoginService;
    private final VerifyUserService verifyUserService;
    private final RefreshVerificationCodeService refreshVerificationCodeService;

    @GetMapping
    public String welcome() {
        return "Welcome to HomeHub! To register, please call /auth/register with POST\n";
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
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
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

    @PatchMapping("/refresh")
    public ResponseEntity<RefreshVerificationCodeResponse> refresh(@RequestBody RefreshVerificationCodeRequest request) {
        if (!request.validate()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid refresh request sent at " + LocalDateTime.now());
        }

        RefreshVerificationCodeCommand cmd = RefreshVerificationCodeCommand
                .builder()
                .email(request.getEmail())
                .build();

        RefreshVerificationCodeDTO dto = refreshVerificationCodeService.execute(cmd);
        return ResponseEntity.status(HttpStatus.OK).body(new RefreshVerificationCodeResponse(dto));
    }

    @PatchMapping("/password")
    public ResponseEntity<ForgotPasswordResponse> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        if (!request.validate()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid forgot password request sent at " + LocalDateTime.now());
        }

        ForgotPasswordCommand cmd = ForgotPasswordCommand
                .builder()
                .email(request.getEmail())
                .build();

        ForgotPasswordDTO dto = forgotPasswordService.execute(cmd);
        return ResponseEntity.status(HttpStatus.OK).body(new ForgotPasswordResponse(dto));
    }

    @PatchMapping("/email/verify")
    public ResponseEntity<VerifyEmailChangeResponse> verifyEmailChange(@RequestBody VerifyEmailChangeRequest request) {
        if (!request.validate()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email change verification request sent at " + LocalDateTime.now());
        }

        VerifyEmailChangeCommand cmd = VerifyEmailChangeCommand
                .builder()
                .emailChangeCode(request.getEmailChangeCode())
                .build();

        UserDTO dto = changeEmailService.execute(cmd);
        return ResponseEntity.status(HttpStatus.OK).body(new VerifyEmailChangeResponse(dto));
    }

    @PatchMapping("/password/reset")
    public ResponseEntity<ResetPasswordResponse> resetPassword(@RequestBody ResetPasswordRequest request) {
        if (!request.validate()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid forgotten password change request sent at " + LocalDateTime.now());
        }

        ResetPasswordCommand cmd = ResetPasswordCommand
                .builder()
                .forgotPasswordCode(request.getForgotPasswordCode())
                .password(request.getPassword())
                .build();

        UserDTO dto = resetPasswordService.execute(cmd);
        return ResponseEntity.status(HttpStatus.OK).body(new ResetPasswordResponse(dto));
    }
}
