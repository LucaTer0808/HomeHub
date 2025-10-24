package com.terfehr.homehub.controller;

import com.terfehr.homehub.application.command.*;
import com.terfehr.homehub.application.dto.ForgotPasswordDTO;
import com.terfehr.homehub.application.dto.RefreshVerificationCodeDTO;
import com.terfehr.homehub.application.dto.UserDTO;
import com.terfehr.homehub.application.dto.UserLoginDTO;
import com.terfehr.homehub.application.service.*;
import com.terfehr.homehub.controller.request.*;
import com.terfehr.homehub.controller.response.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<RegisterUserResponse> register(@Valid @RequestBody RegisterUserRequest request) {
        RegisterUserCommand cmd = RegisterUserCommand
                .builder()
                .username(request.username())
                .email(request.email())
                .password(request.password())
                .confirmPassword(request.confirmPassword())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .build();

        UserDTO registeredUser = registerUserService.execute(cmd);

        return ResponseEntity.status(HttpStatus.CREATED).body(new RegisterUserResponse(registeredUser));
    }

    @PatchMapping("/verify")
    public ResponseEntity<VerifyUserResponse> verify(@Valid @RequestBody VerifyUserRequest request) {
        VerifyUserCommand cmd = VerifyUserCommand
                .builder()
                .verificationCode(request.verificationCode())
                .build();

        UserDTO verifiedUser = verifyUserService.execute(cmd);
        return ResponseEntity.status(HttpStatus.OK).body(new VerifyUserResponse(verifiedUser));
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@Valid @RequestBody UserLoginRequest request) {
        UserLoginCommand cmd = UserLoginCommand
                .builder()
                .email(request.email())
                .password(request.password())
                .build();

        UserLoginDTO dto = userLoginService.execute(cmd);
        return ResponseEntity.status(HttpStatus.OK).body(new UserLoginResponse(dto));
    }

    @PatchMapping("/refresh")
    public ResponseEntity<RefreshVerificationCodeResponse> refresh(@Valid @RequestBody RefreshVerificationCodeRequest request) {
        RefreshVerificationCodeCommand cmd = RefreshVerificationCodeCommand
                .builder()
                .email(request.email())
                .build();

        RefreshVerificationCodeDTO dto = refreshVerificationCodeService.execute(cmd);
        return ResponseEntity.status(HttpStatus.OK).body(new RefreshVerificationCodeResponse(dto));
    }

    @PatchMapping("/password")
    public ResponseEntity<ForgotPasswordResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        ForgotPasswordCommand cmd = ForgotPasswordCommand
                .builder()
                .email(request.email())
                .build();

        ForgotPasswordDTO dto = forgotPasswordService.execute(cmd);
        return ResponseEntity.status(HttpStatus.OK).body(new ForgotPasswordResponse(dto));
    }

    @PatchMapping("/email/verify")
    public ResponseEntity<VerifyEmailChangeResponse> verifyEmailChange(@Valid @RequestBody VerifyEmailChangeRequest request) {
        VerifyEmailChangeCommand cmd = VerifyEmailChangeCommand
                .builder()
                .emailChangeCode(request.emailChangeCode())
                .build();

        UserDTO dto = changeEmailService.execute(cmd);
        return ResponseEntity.status(HttpStatus.OK).body(new VerifyEmailChangeResponse(dto));
    }

    @PatchMapping("/password/reset")
    public ResponseEntity<ResetPasswordResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        ResetPasswordCommand cmd = ResetPasswordCommand
                .builder()
                .forgotPasswordCode(request.forgotPasswordCode())
                .password(request.password())
                .confirmPassword(request.confirmPassword())
                .build();

        UserDTO dto = resetPasswordService.execute(cmd);
        return ResponseEntity.status(HttpStatus.OK).body(new ResetPasswordResponse(dto));
    }
}
