package com.terfehr.homehub.controller;

import com.terfehr.homehub.application.command.ChangeShoppingListNameCommand;
import com.terfehr.homehub.application.command.CreateShoppingListCommand;
import com.terfehr.homehub.application.command.DeleteShoppingListCommand;
import com.terfehr.homehub.application.dto.ShoppingListDTO;
import com.terfehr.homehub.application.service.ChangeShoppingListNameService;
import com.terfehr.homehub.application.service.CreateShoppingListService;
import com.terfehr.homehub.application.service.DeleteShoppingListService;
import com.terfehr.homehub.controller.request.ChangeShoppingListNameRequest;
import com.terfehr.homehub.controller.request.CreateShoppingListRequest;
import com.terfehr.homehub.controller.response.ChangeShoppingListNameResponse;
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

    private final ChangeShoppingListNameService changeShoppingListNameService;
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

    @PatchMapping("/{shoppingListId}/name")
    public ResponseEntity<ChangeShoppingListNameResponse> changeShoppingListName(@PathVariable long shoppingListId, @Valid @RequestBody ChangeShoppingListNameRequest request) {
        ChangeShoppingListNameCommand cmd = ChangeShoppingListNameCommand
                .builder()
                .id(shoppingListId)
                .name(request.name())
                .build();

        ShoppingListDTO dto = changeShoppingListNameService.execute(cmd);
        return ResponseEntity.ok(new ChangeShoppingListNameResponse(dto));
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
