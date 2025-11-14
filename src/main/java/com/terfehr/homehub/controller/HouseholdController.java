package com.terfehr.homehub.controller;

import com.terfehr.homehub.application.command.*;
import com.terfehr.homehub.application.dto.*;
import com.terfehr.homehub.application.service.*;
import com.terfehr.homehub.controller.request.*;
import com.terfehr.homehub.controller.response.*;
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
    private final CreateAccountService createAccountService;
    private final CreateHouseholdService createHouseholdService;
    private final CreateShoppingListService createShoppingListService;
    private final CreateShoppingSpreeService createShoppingSpreeService;
    private final DeleteAccountService deleteAccountService;
    private final DeleteHouseholdService deleteHouseholdService;
    private final DeleteInvitationService deleteInvitationService;
    private final DeleteShoppingListService deleteShoppingListService;
    private final GetHouseholdService getHouseholdService;
    private final InviteUserToHouseholdService inviteUserToHouseholdService;
    private final JoinHouseholdService joinHouseholdService;
    private final LeaveHouseholdService leaveHouseholdService;
    private final TransferAdminRightsService transferAdminRightsService;

    @PostMapping("/")
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

    @PostMapping("/{id}/account")
    public ResponseEntity<CreateAccountResponse> createAccount(@PathVariable long id, @Valid @RequestBody CreateAccountRequest request) {
        CreateAccountCommand cmd = CreateAccountCommand
                .builder()
                .id(id)
                .name(request.name())
                .amount(request.amount())
                .currencyCode(request.currencyCode())
                .build();

        AccountDTO dto = createAccountService.execute(cmd);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateAccountResponse(dto));
    }

    @DeleteMapping("/{householdId}/account/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable long householdId, @PathVariable long accountId) {
        DeleteAccountCommand cmd = DeleteAccountCommand
                .builder()
                .householdId(householdId)
                .accountId(accountId)
                .build();

        deleteAccountService.execute(cmd);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/shopping_list")
    public ResponseEntity<CreateShoppingListResponse> createShoppingList(@PathVariable long id, @Valid @RequestBody CreateShoppingListRequest request) {
        CreateShoppingListCommand cmd = CreateShoppingListCommand
                .builder()
                .id(id)
                .name(request.name())
                .build();

        ShoppingListDTO dto = createShoppingListService.execute(cmd);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateShoppingListResponse(dto));
    }

    @DeleteMapping("/{householdId}/shopping_list{shoppingListId}")
    public ResponseEntity<Void> deleteShoppingList(@PathVariable long householdId, @PathVariable long shoppingListId) {
        DeleteShoppingListCommand cmd = DeleteShoppingListCommand
                .builder()
                .householdId(householdId)
                .shoppingListId(shoppingListId)
                .build();

        deleteShoppingListService.execute(cmd);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/shopping_spree")
    public ResponseEntity<CreateShoppingSpreeResponse> createShoppingSpree(@PathVariable long id, @Valid @RequestBody CreateShoppingSpreeRequest request) {
        CreateShoppingSpreeCommand cmd = CreateShoppingSpreeCommand
                .builder()
                .householdId(id)
                .time(request.time())
                .shoppingListId(request.shoppingListId())
                .accountId(request.accountId())
                .amount(request.amount())
                .description(request.description())
                .recipient(request.recipient())
                .roommateId(request.roommateId())
                .build();

        ShoppingSpreeDTO dto = createShoppingSpreeService.execute(cmd);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateShoppingSpreeResponse(dto));
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
