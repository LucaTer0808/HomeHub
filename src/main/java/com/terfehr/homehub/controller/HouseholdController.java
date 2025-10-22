package com.terfehr.homehub.controller;

import com.terfehr.homehub.application.command.CreateHouseholdCommand;
import com.terfehr.homehub.application.dto.HouseholdDTO;
import com.terfehr.homehub.application.service.CreateHouseholdService;
import com.terfehr.homehub.controller.request.CreateHouseholdRequest;
import com.terfehr.homehub.controller.response.CreateHouseholdResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/household")
@AllArgsConstructor
public class HouseholdController {

    private final CreateHouseholdService createHouseholdService;

    @PostMapping
    public ResponseEntity<CreateHouseholdResponse> createHousehold(@RequestBody CreateHouseholdRequest request) {
        if (!request.validate()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid household creation request sent at " + LocalDateTime.now());
        }

        CreateHouseholdCommand cmd = CreateHouseholdCommand
                .builder()
                .name(request.getName())
                .build();

        HouseholdDTO dto = createHouseholdService.execute(cmd);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateHouseholdResponse(dto));
    }
}
