package com.terfehr.homehub.domain.shopping.service;

import com.terfehr.homehub.domain.shopping.entity.ShoppingList;
import com.terfehr.homehub.domain.shopping.entity.ShoppingListItem;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class ShoppingService {

    /**
     * Finishes the shopping list and returns the items that were picked and then deletes them from it.
     *
     * @param shoppingList The shopping list to be finished.
     * @return The items that were picked and then deleted from the shopping list.
     */
    private Set<ShoppingListItem> finishShoppingList(ShoppingList shoppingList) {
        Set<ShoppingListItem> finishedItems = shoppingList.getPickedItems();
        shoppingList.deletePickedItems();
        return finishedItems;
    }


}
