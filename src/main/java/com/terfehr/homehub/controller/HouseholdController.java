package com.terfehr.homehub.controller;

import com.terfehr.homehub.application.command.*;
import com.terfehr.homehub.application.dto.HouseholdDTO;
import com.terfehr.homehub.application.dto.RoommateDTO;
import com.terfehr.homehub.application.dto.UserInvitationDTO;
import com.terfehr.homehub.application.service.*;
import com.terfehr.homehub.controller.request.*;
import com.terfehr.homehub.controller.response.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/household")
@AllArgsConstructor
public class HouseholdController {

    private final ApplicationEventPublisher publisher;
    private final ChangeHouseholdNameService changeHouseholdNameService;
    private final CreateHouseholdService createHouseholdService;
    private final DeleteHouseholdService deleteHouseholdService;
    private final DeleteInvitationService deleteInvitationService;
    private final GetHouseholdService getHouseholdService;
    private final InviteUserToHouseholdService inviteUserToHouseholdService;
    private final JoinHouseholdService joinHouseholdService;
    private final LeaveHouseholdService leaveHouseholdService;
    private final TransferAdminRightsService transferAdminRightsService;

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

    @PostMapping("/{id}/roommate")
    public ResponseEntity<JoinHouseholdResponse> joinHousehold(@PathVariable long id) {
        JoinHouseholdCommand cmd = JoinHouseholdCommand
                .builder()
                .id(id)
                .build();

        RoommateDTO dto = joinHouseholdService.execute(cmd);
        return ResponseEntity.status(HttpStatus.CREATED).body(new JoinHouseholdResponse(dto));
    }

    @PatchMapping("/{id}/roommate")
    public ResponseEntity<TransferAdminRightsResponse> transferAdminRights(@PathVariable long id, @Valid @RequestBody TransferAdminRightsRequest request) {
        TransferAdminRightsCommand cmd = TransferAdminRightsCommand
                .builder()
                .id(id)
                .email(request.email())
                .build();

        RoommateDTO dto = transferAdminRightsService.execute(cmd);
        return ResponseEntity.status(HttpStatus.OK).body(new TransferAdminRightsResponse(dto));
    }

    @DeleteMapping("{id}/roommate")
    public ResponseEntity<Void> leaveHousehold(@PathVariable long id) {
        LeaveHouseholdCommand cmd = LeaveHouseholdCommand
                .builder()
                .id(id)
                .build();

        leaveHouseholdService.execute(cmd);
        return ResponseEntity.noContent().build();
    }
}
