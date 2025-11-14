package com.terfehr.homehub.application.service;

import com.terfehr.homehub.application.command.ChangeShoppingListNameCommand;
import com.terfehr.homehub.application.dto.ShoppingListDTO;
import com.terfehr.homehub.application.event.ChangeShoppingListNameEvent;
import com.terfehr.homehub.application.event.payload.ChangeShoppingListNameEventPayload;
import com.terfehr.homehub.application.exception.ShoppingListNotFoundException;
import com.terfehr.homehub.domain.shared.exception.InvalidEventPayloadException;
import com.terfehr.homehub.domain.shared.exception.InvalidShoppingListNameException;
import com.terfehr.homehub.domain.shopping.entity.ShoppingList;
import com.terfehr.homehub.domain.shopping.repository.ShoppingListRepositoryInterface;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Transactional
public class ChangeShoppingListNameService {

    private final ApplicationEventPublisher publisher;
    private final ShoppingListRepositoryInterface shoppingListRepository;

    /**
     * Executes the ChangeShoppingListNameCommand by fetching the represented ShoppingList from the Database, updating its name and then publishing an Event that informs about the said update.
     *
     * @param cmd The Command to execute.
     * @return The updated ShoppingList as a DTO.
     * @throws ShoppingListNotFoundException If the ShoppingList to update does not exist.
     * @throws InvalidShoppingListNameException If the name is invalid.
     * @throws InvalidEventPayloadException If the event payload is invalid.
     */
    public ShoppingListDTO execute(ChangeShoppingListNameCommand cmd) throws
            ShoppingListNotFoundException,
            InvalidShoppingListNameException,
            InvalidEventPayloadException
    {
        ShoppingList list = shoppingListRepository.findById(cmd.id())
                .orElseThrow(() -> new ShoppingListNotFoundException("Shopping list not found"));

        list.changeName(cmd.name());

        shoppingListRepository.save(list);

        ChangeShoppingListNameEventPayload payload = new ChangeShoppingListNameEventPayload(cmd.id(), cmd.name());
        ChangeShoppingListNameEvent event = new ChangeShoppingListNameEvent(this, payload);
        publisher.publishEvent(event);

        return new ShoppingListDTO(list.getId(), list.getName());
    }
}
