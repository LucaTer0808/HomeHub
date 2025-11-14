package com.terfehr.homehub.controller;

import com.terfehr.homehub.application.command.CreateAccountCommand;
import com.terfehr.homehub.application.command.DeleteAccountCommand;
import com.terfehr.homehub.application.dto.AccountDTO;
import com.terfehr.homehub.application.service.CreateAccountService;
import com.terfehr.homehub.application.service.DeleteAccountService;
import com.terfehr.homehub.controller.request.CreateAccountRequest;
import com.terfehr.homehub.controller.response.CreateAccountResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
@AllArgsConstructor
public class AccountController {

    private final CreateAccountService createAccountService;
    private final DeleteAccountService deleteAccountService;

    @PostMapping("/{householdId}")
    public ResponseEntity<CreateAccountResponse> createAccount(@PathVariable long householdId, @Valid @RequestBody CreateAccountRequest request) {
        CreateAccountCommand cmd = CreateAccountCommand
                .builder()
                .id(householdId)
                .name(request.name())
                .amount(request.amount())
                .currencyCode(request.currencyCode())
                .build();

        AccountDTO dto = createAccountService.execute(cmd);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateAccountResponse(dto));
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable long accountId) {
        DeleteAccountCommand cmd = DeleteAccountCommand
                .builder()
                .accountId(accountId)
                .build();

        deleteAccountService.execute(cmd);
        return ResponseEntity.noContent().build();
    }

}
