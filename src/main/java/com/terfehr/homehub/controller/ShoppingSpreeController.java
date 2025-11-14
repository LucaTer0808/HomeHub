package com.terfehr.homehub.controller;

import com.terfehr.homehub.application.command.CreateShoppingSpreeCommand;
import com.terfehr.homehub.application.dto.ShoppingSpreeDTO;
import com.terfehr.homehub.application.service.CreateShoppingSpreeService;
import com.terfehr.homehub.controller.request.CreateShoppingSpreeRequest;
import com.terfehr.homehub.controller.response.CreateShoppingSpreeResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shopping-sprees")
@AllArgsConstructor
public class ShoppingSpreeController {

    private final CreateShoppingSpreeService createShoppingSpreeService;

    @PostMapping("/{householdId}")
    public ResponseEntity<CreateShoppingSpreeResponse> createShoppingSpree(@PathVariable long householdId, @Valid @RequestBody CreateShoppingSpreeRequest request) {
        CreateShoppingSpreeCommand cmd = CreateShoppingSpreeCommand
                .builder()
                .householdId(householdId)
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
}
