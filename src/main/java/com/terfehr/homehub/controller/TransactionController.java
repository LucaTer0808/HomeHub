package com.terfehr.homehub.controller;

import com.terfehr.homehub.application.command.CreateExpenseCommand;
import com.terfehr.homehub.application.dto.ExpenseDTO;
import com.terfehr.homehub.application.service.CreateExpenseService;
import com.terfehr.homehub.controller.request.CreateExpenseRequest;
import com.terfehr.homehub.controller.response.CreateExpenseResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Handles financial transactions and NOT database transactions!
 */
@RestController
@RequestMapping("/transactions")
@AllArgsConstructor
public class TransactionController {

    private final CreateExpenseService createExpenseService;

    @PostMapping("/{accountId}/expense")
    public ResponseEntity<CreateExpenseResponse> createExpense(@PathVariable long accountId, @Valid @RequestBody CreateExpenseRequest request) {
        CreateExpenseCommand cmd = CreateExpenseCommand
                .builder()
                .accountId(accountId)
                .amount(request.amount())
                .description(request.description())
                .date(request.date())
                .recipient(request.recipient())
                .build();

        ExpenseDTO dto = createExpenseService.execute(cmd);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateExpenseResponse(dto));
    }
}
