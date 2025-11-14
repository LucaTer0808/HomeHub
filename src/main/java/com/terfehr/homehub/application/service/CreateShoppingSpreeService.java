package com.terfehr.homehub.application.service;

import com.terfehr.homehub.application.command.CreateShoppingSpreeCommand;
import com.terfehr.homehub.application.dto.ShoppingSpreeDTO;
import com.terfehr.homehub.application.dto.ShoppingSpreeItemDTO;
import com.terfehr.homehub.application.event.CreateShoppingSpreeEvent;
import com.terfehr.homehub.application.event.payload.CreateShoppingSpreeEventPayload;
import com.terfehr.homehub.application.exception.AccountNotFoundException;
import com.terfehr.homehub.application.exception.HouseholdNotFoundException;
import com.terfehr.homehub.application.exception.RoommateNotFoundException;
import com.terfehr.homehub.application.exception.ShoppingListNotFoundException;
import com.terfehr.homehub.application.interfaces.AuthUserProviderInterface;
import com.terfehr.homehub.domain.bookkeeping.entity.Account;
import com.terfehr.homehub.domain.bookkeeping.repository.AccountRepositoryInterface;
import com.terfehr.homehub.domain.household.entity.Household;
import com.terfehr.homehub.domain.household.entity.Roommate;
import com.terfehr.homehub.domain.household.entity.User;
import com.terfehr.homehub.domain.household.repository.HouseholdRepositoryInterface;
import com.terfehr.homehub.domain.household.repository.RoommateRepositoryInterface;
import com.terfehr.homehub.domain.shared.exception.*;
import com.terfehr.homehub.domain.shopping.entity.ShoppingList;
import com.terfehr.homehub.domain.shopping.entity.ShoppingSpree;
import com.terfehr.homehub.domain.shopping.entity.ShoppingSpreeItem;
import com.terfehr.homehub.domain.shopping.repository.ShoppingListRepositoryInterface;
import com.terfehr.homehub.domain.shopping.service.ShoppingSpreeService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
@Transactional
public class CreateShoppingSpreeService {

    private final AccountRepositoryInterface accountRepository;
    private final ApplicationEventPublisher publisher;
    private final AuthUserProviderInterface userProvider;
    private final HouseholdRepositoryInterface householdRepository;
    private final RoommateRepositoryInterface roommateRepository;
    private final ShoppingListRepositoryInterface shoppingListRepository;
    private final ShoppingSpreeService shoppingSpreeService;

    /**
     * Executed the given CreateShoppingSpreeCommand by creating a ShoppingSpree and then publishing an Event that informs about said creation.
     * It also creates a ShoppingExpense that represents the Transaction done to make this purchase.
     *
     * @param cmd The Command to execute.
     * @return A ShoppingSpreeDTO containing the newly created ShoppingSpree with id and date as well as the id of the created ShoppingExpense.
     * @throws HouseholdNotFoundException If the Household to add the ShoppingSpree to does not exist.
     * @throws ShoppingListNotFoundException If the ShoppingList to add the ShoppingSpreeItem to does not exist.
     * @throws AccountNotFoundException If the Account to add the ShoppingSpreeItem to does not exist.
     * @throws RoommateNotFoundException If the Roommate for the given Household and User does not exist.
     * @throws InvalidShoppingListException If the given ShoppingList is invalid. It has to belong to the given Household.
     * @throws InvalidAccountException If the given Account is invalid. It has to belong to the given Household.
     * @throws InvalidAmountException If the given amount is invalid. It most likely is negative.
     * @throws InvalidRoommateException If the given Roommate is invalid. It has to belong to the given Household.
     * @throws InvalidDateException If the given date is invalid.
     * @throws InvalidDescriptionException If the given description is invalid.
     * @throws InvalidRecipientException If the given recipient is invalid.
     * @throws InvalidHouseholdException If the given Household is invalid.
     * @throws InvalidShoppingSpreeException If the given ShoppingSpree is invalid. This should not occur since the ShoppingSpree is just created here.
     * @throws InvalidShoppingExpenseException If the given ShoppingExpense is invalid.
     */
    public ShoppingSpreeDTO execute(CreateShoppingSpreeCommand cmd) throws
            HouseholdNotFoundException,
            ShoppingListNotFoundException,
            AccountNotFoundException,
            RoommateNotFoundException,
            InvalidShoppingListException,
            InvalidAccountException,
            InvalidAmountException,
            InvalidRoommateException,
            InvalidDateException,
            InvalidDescriptionException,
            InvalidRecipientException,
            InvalidHouseholdException,
            InvalidShoppingSpreeException,
            InvalidShoppingExpenseException
    {
        User user = userProvider.getUser();

        Household household = householdRepository.findById(cmd.householdId())
                .orElseThrow(() -> new HouseholdNotFoundException("Household not found with ID: " + cmd.householdId()));

        ShoppingList shoppingList = shoppingListRepository.findById(cmd.shoppingListId())
                .orElseThrow(() -> new ShoppingListNotFoundException("There is no ShoppingList with ID: " + cmd.shoppingListId()));

        Account account = accountRepository.findById(cmd.accountId())
                .orElseThrow(() -> new AccountNotFoundException("There is no Account with ID: " + cmd.accountId()));

        Roommate roommate = roommateRepository.findByHouseholdIdAndUserEmail(cmd.householdId(), user.getEmail())
                .orElseThrow(() -> new RoommateNotFoundException("There is no Roommate for the given Household and User"));

        ShoppingSpree shoppingSpree = shoppingSpreeService.create(household, cmd.time(), shoppingList, account, cmd.amount(), cmd.description(), cmd.recipient(), roommate);

        householdRepository.save(household);

        CreateShoppingSpreeEventPayload payload = new CreateShoppingSpreeEventPayload(shoppingSpree.getId(), shoppingSpree.getDate());
        CreateShoppingSpreeEvent event = new CreateShoppingSpreeEvent(this, payload);
        publisher.publishEvent(event);

        Set<ShoppingSpreeItemDTO> items = createItemDTOs(shoppingSpree);
        return new ShoppingSpreeDTO(shoppingSpree.getId(), shoppingSpree.getDate(), items, shoppingSpree.getShoppingExpense().getId());
    }

    /**
     * Transforms the given ShoppingSpree into a Set of ShoppingSpreeItemDTOs.
     *
     * @param shoppingSpree The ShoppingSpree to transform.
     * @return A Set of ShoppingSpreeItemDTOs.
     */
    private Set<ShoppingSpreeItemDTO> createItemDTOs(ShoppingSpree shoppingSpree) {
        Set<ShoppingSpreeItemDTO> items = new HashSet<>();

        for (ShoppingSpreeItem item : shoppingSpree.getShoppingSpreeItems()) {
            items.add(new ShoppingSpreeItemDTO(item.getName(), item.getQuantity()));
        }

        return items;
    }
}
