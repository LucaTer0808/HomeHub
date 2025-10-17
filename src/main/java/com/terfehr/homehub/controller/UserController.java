package com.terfehr.homehub.controller;

import com.terfehr.homehub.application.command.ChangeEmailCommand;
import com.terfehr.homehub.application.dto.ChangeEmailDTO;
import com.terfehr.homehub.application.service.ChangeEmailService;
import com.terfehr.homehub.controller.request.ChangeEmailRequest;
import com.terfehr.homehub.controller.response.ChangeEmailResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final ChangeEmailService changeEmailService;

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

}
