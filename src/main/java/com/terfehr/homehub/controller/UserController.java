package com.terfehr.homehub.controller;

import com.terfehr.homehub.application.command.*;
import com.terfehr.homehub.application.dto.ChangeEmailDTO;
import com.terfehr.homehub.application.dto.ChangeUsernameDTO;
import com.terfehr.homehub.application.dto.DeleteUserDTO;
import com.terfehr.homehub.application.dto.UserDTO;
import com.terfehr.homehub.application.service.*;
import com.terfehr.homehub.controller.request.*;
import com.terfehr.homehub.controller.response.ChangeEmailResponse;
import com.terfehr.homehub.controller.response.ChangeNameResponse;
import com.terfehr.homehub.controller.response.ChangeUsernameResponse;
import com.terfehr.homehub.controller.response.DeleteUserResponse;
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
    public ResponseEntity<ChangeUsernameResponse> changeUsername(@RequestBody ChangeUsernameRequest request) {
        if (!request.validate()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid username change request sent at " + LocalDateTime.now());
        }

        ChangeUsernameCommand cmd = ChangeUsernameCommand
                .builder()
                .username(request.getUsername())
                .build();

        ChangeUsernameDTO dto = changeUsernameService.execute(cmd);
        return ResponseEntity.status(HttpStatus.OK).body(new ChangeUsernameResponse(dto));
    }

    @PatchMapping("/email")
    public ResponseEntity<ChangeEmailResponse> changeEmail(@RequestBody ChangeEmailRequest request) {
        if (!request.validate()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email change request sent at " + LocalDateTime.now());
        }

        ChangeEmailCommand cmd = ChangeEmailCommand
                .builder()
                .email(request.getEmail())
                .build();

        ChangeEmailDTO dto = changeEmailService.execute(cmd);
        return ResponseEntity.status(HttpStatus.OK).body(new ChangeEmailResponse(dto));
    }

    @PatchMapping("/password")
    public ResponseEntity<ChangePasswordResponse> changePassword(@RequestBody ChangePasswordRequest request) {
        if (!request.validate()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid password change request sent at " + LocalDateTime.now());
        }

        ChangePasswordCommand cmd = ChangePasswordCommand
                .builder()
                .password(request.getPassword())
                .build();

        UserDTO user = changePasswordService.execute(cmd);
        return ResponseEntity.status(HttpStatus.OK).body(new ChangePasswordResponse(user));
    }

    @PatchMapping("/name")
    public ResponseEntity<ChangeNameResponse> changeName(@RequestBody ChangeNameRequest request) {
        if (!request.validate()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid name change request sent at " + LocalDateTime.now());
        }

        ChangeNameCommand cmd = ChangeNameCommand
                .builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();

        UserDTO dto = changeNameService.execute(cmd);
        return ResponseEntity.status(HttpStatus.OK).body(new ChangeNameResponse(dto));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@RequestBody DeleteUserRequest request) {
        if (!request.validate()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid delete user request sent at " + LocalDateTime.now());
        }

        DeleteUserCommand cmd = DeleteUserCommand
                .builder()
                .password(request.getPassword())
                .build();

        deleteUserService.execute(cmd);
        return ResponseEntity.noContent().build();
    }
}
