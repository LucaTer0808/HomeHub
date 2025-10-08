package com.terfehr.homehub.domain.shopping.service;

import com.terfehr.homehub.domain.bookkeeping.entity.Account;
import com.terfehr.homehub.domain.household.entity.Household;
import com.terfehr.homehub.domain.shopping.entity.ShoppingList;
import com.terfehr.homehub.domain.shopping.entity.ShoppingListItem;
import com.terfehr.homehub.domain.shopping.entity.ShoppingSpree;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@AllArgsConstructor
public class ShoppingService {

    /**
     * Clears the shopping list of all picked items and creates a new ShoppingSpree based on the items that were picked.
     * This ShoppingSpree is then returned.
     *
     * @param shoppingList The shopping list to be finished.
     * @return The items that were picked and then deleted from the shopping list.
     */
    public ShoppingSpree prepareShoppingSpree(ShoppingList shoppingList, Household household) {
        Set<ShoppingListItem> pickedItems = shoppingList.getPickedItems();
        shoppingList.deletePickedItems();

        ShoppingSpree spree = household.addShoppingSpree(LocalDateTime.now());
        populateShoppingSpree(spree, pickedItems);
        return spree;
    }

    /**
     * Populates the ShoppingSpree with the given ShoppingListItems. The ShoppingListItems are added to the given ShoppingSpree
     * by converting their name and quantity to ShoppingSpreeItems.
     *
     * @param spree The ShoppingSpree to populate.
     * @param finishedItems The ShoppingListItems to populate the ShoppingSpree with.
     */
    private void populateShoppingSpree(ShoppingSpree spree, Set<ShoppingListItem> finishedItems) {
        for (ShoppingListItem item : finishedItems) {
            spree.addShoppingSpreeItem(item.getName(), item.getQuantity());
        }
    }
}
