package com.terfehr.homehub.domain.shopping.service;

import com.terfehr.homehub.domain.bookkeeping.entity.Account;
import com.terfehr.homehub.domain.bookkeeping.entity.ShoppingExpense;
import com.terfehr.homehub.domain.household.entity.Household;
import com.terfehr.homehub.domain.household.entity.Roommate;
import com.terfehr.homehub.domain.shared.exception.*;
import com.terfehr.homehub.domain.shopping.entity.ShoppingList;
import com.terfehr.homehub.domain.shopping.entity.ShoppingListItem;
import com.terfehr.homehub.domain.shopping.entity.ShoppingSpree;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ShoppingSpreeService {

    /**
     * Creates a finished ShoppingSpree by first creating the object itself, then adding all items from the given ShoppingList, removing the picked items
     * from the list, creating a ShoppingSpreeTransaction from the given account and adding it to the ShoppingSpree.
     *
     * @param household The Household to create the ShoppingSpree for.
     * @param time The time of the ShoppingSpree.
     * @param shoppingList The ShoppingList to convert to ShoppingSpreeItems.
     * @param account The Account that the money for the Transaction was used from.
     * @param amount The amount of the ShoppingSpreeTransaction.
     * @param description The description of the ShoppingSpreeTransaction.
     * @param recipient The recipient of the ShoppingSpreeTransaction.
     * @param roommate The Roommate that the ShoppingSpreeTransaction was used for.
     * @return The newly created ShoppingSpree.
     * @throws InvalidShoppingListException If the given ShoppingList does not belong to the given Household.
     * @throws InvalidAccountException If the given Account does not belong to the given Household.
     * @throws InvalidRoommateException If the given Roommate does not belong to the given Household.
     * @throws InvalidDateException If the given time is invalid.
     * @throws InvalidHouseholdException If the given Household is invalid.
     * @throws InvalidShoppingExpenseException If the created ShoppingExpense is invalid. Should not happen.
     * @throws InvalidShoppingSpreeException If the created ShoppingSpree is invalid. Should not happen.
     * @throws InvalidDescriptionException If the given description is invalid.
     * @throws InvalidRecipientException If the given recipient is invalid.
     * @throws InvalidAmountException If the given amount is invalid.
     */
    public ShoppingSpree create(Household household, LocalDateTime time, ShoppingList shoppingList, Account account, long amount, String description, String recipient, Roommate roommate) throws
            InvalidShoppingListException,
            InvalidAccountException,
            InvalidAmountException,
            InvalidRoommateException,
            InvalidDateException,
            InvalidDescriptionException,
            InvalidRecipientException,
            InvalidHouseholdException,
            InvalidShoppingExpenseException,
            InvalidShoppingSpreeException
    {
        if (!shoppingList.getHousehold().equals(household)) {
            throw new InvalidShoppingListException("ShoppingList does not belong to the given Household");
        }
        if (!account.getHousehold().equals(household)) {
            throw new InvalidAccountException("Account does not belong to the given Household");
        }
        if (!roommate.getHousehold().equals(household)) {
            throw new InvalidRoommateException("Roommate does not belong to the given Household");
        }

        ShoppingSpree spree = household.createShoppingSpree(time);

        convertItems(shoppingList, spree);

        ShoppingExpense expense = account.addShoppingExpense(amount, description, time, recipient, roommate);
        spree.setShoppingExpense(expense);
        expense.setShoppingSpree(spree);

        return spree;
    }

    /**
     * Transports the item names and quantities from the given ShoppingList to the given ShoppingSpree.
     *
     * @param list The ShoppingList to convert.
     * @param spree The ShoppingSpree to convert the items to.
     */
    private void convertItems(ShoppingList list, ShoppingSpree spree) {
        for (ShoppingListItem item : list.getShoppingListItems()) {
            if (item.isPicked()) {
                spree.addShoppingSpreeItem(item.getName(), item.getQuantity());
                list.removeItem(item);
            }
        }
    }
}
