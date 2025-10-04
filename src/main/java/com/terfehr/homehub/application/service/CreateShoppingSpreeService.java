package com.terfehr.homehub.application.service;

import com.terfehr.homehub.domain.bookkeeping.entity.Account;
import com.terfehr.homehub.domain.bookkeeping.entity.ShoppingExpense;
import com.terfehr.homehub.domain.bookkeeping.exception.AccountNotFoundException;
import com.terfehr.homehub.domain.bookkeeping.repository.AccountRepositoryInterface;
import com.terfehr.homehub.domain.bookkeeping.service.BookkeepingService;
import com.terfehr.homehub.domain.household.entity.Household;
import com.terfehr.homehub.domain.household.exception.HouseholdNotFoundException;
import com.terfehr.homehub.domain.household.repository.HouseholdRepositoryInterface;
import com.terfehr.homehub.domain.shopping.entity.ShoppingList;
import com.terfehr.homehub.domain.shopping.entity.ShoppingSpree;
import com.terfehr.homehub.domain.shopping.exception.ShoppingListNotFoundException;
import com.terfehr.homehub.domain.shopping.repository.ShoppingListRepositoryInterface;
import com.terfehr.homehub.domain.shopping.service.ShoppingService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateShoppingSpreeService {

    private final HouseholdRepositoryInterface householdRepository;
    private final ShoppingListRepositoryInterface shoppingListRepository;
    private final AccountRepositoryInterface accountRepository;

    private ShoppingService shoppingService;

    /**
     * Executes the CreateShoppingSpreeCommand by creating a ShoppingSpree and associating it with an Account.
     *
     * @param cmd The CreateShoppingSpreeCommand to execute containing the necessary information for creating a ShoppingSpree.
     * @return The newly created ShoppingSpree.
     * @throws HouseholdNotFoundException If the Household with the given ID does not exist.
     * @throws ShoppingListNotFoundException If the ShoppingList with the given ID does not exist.
     * @throws AccountNotFoundException If the Account with the given ID does not exist.
     */
    @Transactional
    public ShoppingSpree execute(CreateShoppingSpreeCommand cmd) throws HouseholdNotFoundException, ShoppingListNotFoundException, AccountNotFoundException{
        Household household = householdRepository.findById(cmd.getHouseholdId())
                .orElseThrow(() -> new HouseholdNotFoundException("Household with ID " + cmd.getHouseholdId() + " not found"));

        ShoppingList shoppingList = shoppingListRepository.findById(cmd.getShoppingListId())
                .orElseThrow(() -> new ShoppingListNotFoundException("ShoppingList with ID " + cmd.getShoppingListId() + " not found"));

        Account account = accountRepository.findById(cmd.getAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Account  with the ID " + cmd.getAccountId() + " not found"));

        ShoppingSpree spree = shoppingService.prepareShoppingSpree(shoppingList, household);
        ShoppingExpense expense = account.addShoppingExpense(cmd.getAmount(), cmd.getDescription(), cmd.getDate(), cmd.getRecipient());
        spree.setShoppingExpense(expense);
        expense.setShoppingSpree(spree);

        householdRepository.save(household);
        return spree;
    }
}
