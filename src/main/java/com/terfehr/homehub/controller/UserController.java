package com.terfehr.homehub.controller;

import com.terfehr.homehub.application.command.*;
import com.terfehr.homehub.application.dto.ChangeEmailDTO;
import com.terfehr.homehub.application.dto.ChangeUsernameDTO;
import com.terfehr.homehub.application.dto.UserDTO;
import com.terfehr.homehub.application.service.*;
import com.terfehr.homehub.controller.request.*;
import com.terfehr.homehub.controller.response.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final ChangeEmailService changeEmailService;
    private final ChangeNameService changeNameService;
    private final ChangePasswordService changePasswordService;
    private final ChangeUsernameService changeUsernameService;
    private final DeleteUserService deleteUserService;
    private final GetUserService getUserService;

    @GetMapping
    public ResponseEntity<UserDTO> getUser() {
        UserDTO user = getUserService.execute();
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PatchMapping("/username")
    public ResponseEntity<ChangeUsernameResponse> changeUsername(@Valid @RequestBody ChangeUsernameRequest request) {
        ChangeUsernameCommand cmd = ChangeUsernameCommand
                .builder()
                .username(request.username())
                .build();

        ChangeUsernameDTO dto = changeUsernameService.execute(cmd);
        return ResponseEntity.status(HttpStatus.OK).body(new ChangeUsernameResponse(dto));
    }

    @PatchMapping("/email")
    public ResponseEntity<ChangeEmailResponse> changeEmail(@Valid @RequestBody ChangeEmailRequest request) {
        ChangeEmailCommand cmd = ChangeEmailCommand
                .builder()
                .email(request.email())
                .build();

        ChangeEmailDTO dto = changeEmailService.execute(cmd);
        return ResponseEntity.status(HttpStatus.OK).body(new ChangeEmailResponse(dto));
    }

    @PatchMapping("/password")
    public ResponseEntity<ChangePasswordResponse> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        ChangePasswordCommand cmd = ChangePasswordCommand
                .builder()
                .password(request.password())
                .confirmPassword(request.confirmPassword())
                .build();

        UserDTO user = changePasswordService.execute(cmd);
        return ResponseEntity.status(HttpStatus.OK).body(new ChangePasswordResponse(user));
    }

    @PatchMapping("/name")
    public ResponseEntity<ChangeNameResponse> changeName(@Valid @RequestBody ChangeNameRequest request) {
        ChangeNameCommand cmd = ChangeNameCommand
                .builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .build();

        UserDTO dto = changeNameService.execute(cmd);
        return ResponseEntity.status(HttpStatus.OK).body(new ChangeNameResponse(dto));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@Valid @RequestBody DeleteUserRequest request) {
        DeleteUserCommand cmd = DeleteUserCommand
                .builder()
                .password(request.password())
                .build();

        deleteUserService.execute(cmd);
        return ResponseEntity.noContent().build();
    }
}
