package com.terfehr.homehub.application.service;

import com.terfehr.homehub.application.command.CreateShoppingListCommand;
import com.terfehr.homehub.application.dto.ShoppingListDTO;
import com.terfehr.homehub.application.event.CreateShoppingListEvent;
import com.terfehr.homehub.application.event.payload.CreateShoppingListEventPayload;
import com.terfehr.homehub.application.exception.HouseholdNotFoundException;
import com.terfehr.homehub.domain.household.entity.Household;
import com.terfehr.homehub.domain.household.repository.HouseholdRepositoryInterface;
import com.terfehr.homehub.domain.shared.exception.InvalidShoppingListNameException;
import com.terfehr.homehub.domain.shopping.entity.ShoppingList;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateShoppingListService {

    private final ApplicationEventPublisher publisher;
    private final HouseholdRepositoryInterface householdRepository;

    /**
     * Executed the given CreateShoppingListCommand by creating a new ShoppingList and adding it to the given Household.
     *
     * @param cmd The Command to execute. Contains the ID of the Household and the name of the ShoppingList to create.
     * @return The created ShoppingList as a DTO.
     * @throws HouseholdNotFoundException If the Household to add the ShoppingList to does not exist.
     * @throws InvalidShoppingListNameException If the given ShoppingList name is invalid.
     */
    public ShoppingListDTO execute(CreateShoppingListCommand cmd) throws
            HouseholdNotFoundException,
            InvalidShoppingListNameException
    {
        Household household = householdRepository.findById(cmd.id())
                .orElseThrow(() -> new HouseholdNotFoundException("There is no Household with the ID: " + cmd.id()));

        ShoppingList list = household.createShoppingList(cmd.name());

        householdRepository.save(household);

        CreateShoppingListEventPayload payload = new CreateShoppingListEventPayload(household.getId(), list.getId(), list.getName());
        CreateShoppingListEvent event = new CreateShoppingListEvent(this, payload);
        publisher.publishEvent(event);

        return new ShoppingListDTO(list.getId(), list.getName());
    }
}
