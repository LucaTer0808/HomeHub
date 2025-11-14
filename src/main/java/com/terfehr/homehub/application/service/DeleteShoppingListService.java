package com.terfehr.homehub.application.service;

import com.terfehr.homehub.application.command.DeleteShoppingListCommand;
import com.terfehr.homehub.application.event.DeleteShoppingListEvent;
import com.terfehr.homehub.application.event.payload.DeleteShoppingListEventPayload;
import com.terfehr.homehub.application.exception.HouseholdNotFoundException;
import com.terfehr.homehub.application.exception.ShoppingListNotFoundException;
import com.terfehr.homehub.domain.household.entity.Household;
import com.terfehr.homehub.domain.household.repository.HouseholdRepositoryInterface;
import com.terfehr.homehub.domain.shopping.entity.ShoppingList;
import com.terfehr.homehub.domain.shopping.repository.ShoppingListRepositoryInterface;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Transactional
public class DeleteShoppingListService {

    private final ApplicationEventPublisher publisher;
    private final HouseholdRepositoryInterface householdRepository;
    private final ShoppingListRepositoryInterface shoppingListRepository;

    public void execute(DeleteShoppingListCommand cmd) {
        ShoppingList list = shoppingListRepository.findById(cmd.shoppingListId())
                .orElseThrow(() -> new ShoppingListNotFoundException("There is no shopping list with ID: " + cmd.shoppingListId()));

        Household household = list.getHousehold();

        household.deleteShoppingList(list);

        householdRepository.save(household);

        DeleteShoppingListEventPayload payload = new DeleteShoppingListEventPayload(household.getId(), list.getId(), list.getName());
        DeleteShoppingListEvent event = new DeleteShoppingListEvent(this, payload);
        publisher.publishEvent(event);
    }
}
