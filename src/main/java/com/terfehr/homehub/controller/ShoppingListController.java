package com.terfehr.homehub.controller;

import com.terfehr.homehub.application.command.CreateShoppingListCommand;
import com.terfehr.homehub.application.command.DeleteShoppingListCommand;
import com.terfehr.homehub.application.dto.ShoppingListDTO;
import com.terfehr.homehub.application.service.CreateShoppingListService;
import com.terfehr.homehub.application.service.DeleteShoppingListService;
import com.terfehr.homehub.controller.request.CreateShoppingListRequest;
import com.terfehr.homehub.controller.response.CreateShoppingListResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shopping-lists")
@AllArgsConstructor
public class ShoppingListController {

    private final CreateShoppingListService createShoppingListService;
    private final DeleteShoppingListService deleteShoppingListService;

    @PostMapping("/{householdId}")
    public ResponseEntity<CreateShoppingListResponse> createShoppingList(@PathVariable long householdId, @Valid @RequestBody CreateShoppingListRequest request) {
        CreateShoppingListCommand cmd = CreateShoppingListCommand
                .builder()
                .id(householdId)
                .name(request.name())
                .build();

        ShoppingListDTO dto = createShoppingListService.execute(cmd);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateShoppingListResponse(dto));
    }

    @DeleteMapping("/{shoppingListId}")
    public ResponseEntity<Void> deleteShoppingList(@PathVariable long shoppingListId) {
        DeleteShoppingListCommand cmd = DeleteShoppingListCommand
                .builder()
                .shoppingListId(shoppingListId)
                .build();

        deleteShoppingListService.execute(cmd);
        return ResponseEntity.noContent().build();
    }

}
