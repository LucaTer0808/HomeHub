package com.terfehr.homehub.controller;

import com.terfehr.homehub.application.command.CreateExpenseCommand;
import com.terfehr.homehub.application.command.CreateIncomeCommand;
import com.terfehr.homehub.application.command.DeleteTransactionCommand;
import com.terfehr.homehub.application.dto.ExpenseDTO;
import com.terfehr.homehub.application.dto.IncomeDTO;
import com.terfehr.homehub.application.service.CreateExpenseService;
import com.terfehr.homehub.application.service.CreateIncomeService;
import com.terfehr.homehub.application.service.DeleteTransactionService;
import com.terfehr.homehub.controller.request.CreateExpenseRequest;
import com.terfehr.homehub.controller.request.CreateIncomeRequest;
import com.terfehr.homehub.controller.response.CreateExpenseResponse;
import com.terfehr.homehub.controller.response.CreateIncomeResponse;
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
    private final CreateIncomeService createIncomeService;
    private final DeleteTransactionService deleteTransactionService;

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

    @PostMapping("/{accountId}/income")
    public ResponseEntity<CreateIncomeResponse> createIncome(@PathVariable long accountId, @Valid @RequestBody CreateIncomeRequest request) {
        CreateIncomeCommand cmd = CreateIncomeCommand
                .builder()
                .accountId(accountId)
                .amount(request.amount())
                .description(request.description())
                .date(request.date())
                .source(request.source())
                .build();

        IncomeDTO dto = createIncomeService.execute(cmd);
        return ResponseEntity.ok(new CreateIncomeResponse(dto));
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable long transactionId) {
        DeleteTransactionCommand cmd = DeleteTransactionCommand
                .builder()
                .transactionId(transactionId)
                .build();

        deleteTransactionService.execute(cmd);
        return ResponseEntity.noContent().build();
    }
}
