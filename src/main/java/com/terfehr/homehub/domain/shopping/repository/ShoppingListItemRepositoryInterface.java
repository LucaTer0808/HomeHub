package com.terfehr.homehub.domain.shopping.repository;

import com.terfehr.homehub.domain.shopping.entity.ShoppingListItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface ShoppingListItemRepositoryInterface extends JpaRepository<ShoppingListItem, Long> {

    /**
     * Retrieves a ShoppingListItem by its ID.
     *
     * @param id The ID of the ShoppingListItem.
     * @return An Optional containing either the ShoppingListItem or null if it does not exist.
     */
    @NonNull
    Optional<ShoppingListItem> findById(@NonNull Long id);

    /**
     * Retrieves all ShoppingListItems for a given ShoppingList.
     *
     * @param shoppingListId The ID of the ShoppingList.
     * @return A List of ShoppingListItems.
     */
    List<ShoppingListItem> findAllByShoppingListId(Long shoppingListId);
}
