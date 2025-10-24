package com.terfehr.homehub.controller;

import com.terfehr.homehub.application.command.ChangeHouseholdNameCommand;
import com.terfehr.homehub.application.command.CreateHouseholdCommand;
import com.terfehr.homehub.application.command.DeleteHouseholdCommand;
import com.terfehr.homehub.application.command.GetHouseholdCommand;
import com.terfehr.homehub.application.dto.HouseholdDTO;
import com.terfehr.homehub.application.service.ChangeHouseholdNameService;
import com.terfehr.homehub.application.service.CreateHouseholdService;
import com.terfehr.homehub.application.service.DeleteHouseholdService;
import com.terfehr.homehub.application.service.GetHouseholdService;
import com.terfehr.homehub.controller.request.ChangeHouseholdNameRequest;
import com.terfehr.homehub.controller.request.CreateHouseholdRequest;
import com.terfehr.homehub.controller.response.ChangeHouseholdNameResponse;
import com.terfehr.homehub.controller.response.CreateHouseholdResponse;
import com.terfehr.homehub.controller.response.GetHouseholdResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/household")
@AllArgsConstructor
public class HouseholdController {

    private final ChangeHouseholdNameService changeHouseholdNameService;
    private final CreateHouseholdService createHouseholdService;
    private final DeleteHouseholdService deleteHouseholdService;
    private final GetHouseholdService getHouseholdService;

    @PostMapping
    public ResponseEntity<CreateHouseholdResponse> createHousehold(@Valid @RequestBody CreateHouseholdRequest request) {
        CreateHouseholdCommand cmd = CreateHouseholdCommand
                .builder()
                .name(request.name())
                .build();

        HouseholdDTO dto = createHouseholdService.execute(cmd);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateHouseholdResponse(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetHouseholdResponse> getHousehold(@PathVariable long id) {
        GetHouseholdCommand cmd = GetHouseholdCommand
                .builder()
                .id(id)
                .build();

        HouseholdDTO dto = getHouseholdService.execute(cmd);
        return ResponseEntity.status(HttpStatus.OK).body(new GetHouseholdResponse(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHousehold(@PathVariable long id) {
        DeleteHouseholdCommand cmd = DeleteHouseholdCommand
                .builder()
                .id(id)
                .build();

        deleteHouseholdService.execute(cmd);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/name")
    public ResponseEntity<ChangeHouseholdNameResponse> changeHouseholdName(@PathVariable long id, @Valid @RequestBody ChangeHouseholdNameRequest request) {
        ChangeHouseholdNameCommand cmd = ChangeHouseholdNameCommand
                .builder()
                .id(id)
                .name(request.name())
                .build();

        HouseholdDTO dto = changeHouseholdNameService.execute(cmd);
        return ResponseEntity.status(HttpStatus.OK).body(new ChangeHouseholdNameResponse(dto));
    }
}
