package com.terfehr.homehub.controller;

import com.terfehr.homehub.application.command.*;
import com.terfehr.homehub.application.dto.HouseholdDTO;
import com.terfehr.homehub.application.dto.UserInvitationDTO;
import com.terfehr.homehub.application.service.*;
import com.terfehr.homehub.controller.request.ChangeHouseholdNameRequest;
import com.terfehr.homehub.controller.request.CreateHouseholdRequest;
import com.terfehr.homehub.controller.request.DeleteInvitationRequest;
import com.terfehr.homehub.controller.request.InviteUserToHouseholdRequest;
import com.terfehr.homehub.controller.response.ChangeHouseholdNameResponse;
import com.terfehr.homehub.controller.response.CreateHouseholdResponse;
import com.terfehr.homehub.controller.response.GetHouseholdResponse;
import com.terfehr.homehub.controller.response.InviteUserToHouseholdResponse;
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
    private final DeleteInvitationService deleteInvitationService;
    private final GetHouseholdService getHouseholdService;
    private final InviteUserToHouseholdService inviteUserToHouseholdService;

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

    @PostMapping("/{id}/invitation")
    public ResponseEntity<InviteUserToHouseholdResponse> inviteUserToHousehold(@PathVariable long id, @Valid @RequestBody InviteUserToHouseholdRequest request) {
        InviteUserToHouseholdCommand cmd = InviteUserToHouseholdCommand
                .builder()
                .id(id)
                .email(request.email())
                .build();

        UserInvitationDTO dto = inviteUserToHouseholdService.execute(cmd);
        return ResponseEntity.status(HttpStatus.CREATED).body(new InviteUserToHouseholdResponse(dto));
    }

    @DeleteMapping("/{id}/invitation")
    public ResponseEntity<Void> deleteInvitation(@PathVariable long id, @Valid @RequestBody DeleteInvitationRequest request) {
        DeleteInvitationCommand cmd = DeleteInvitationCommand
                .builder()
                .id(id)
                .email(request.email())
                .build();

        deleteInvitationService.execute(cmd);
        return ResponseEntity.noContent().build();
    }
}
