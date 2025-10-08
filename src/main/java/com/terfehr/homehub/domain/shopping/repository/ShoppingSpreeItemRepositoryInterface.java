package com.terfehr.homehub.domain.shopping.repository;

import com.terfehr.homehub.domain.shopping.entity.ShoppingSpreeItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface ShoppingSpreeItemRepositoryInterface extends JpaRepository<ShoppingSpreeItem, Long> {

    /**
     * Retrieves a ShoppingSpreeItem by its ID.
     *
     * @param id The ID of the ShoppingSpreeItem.
     * @return An Optional containing either the ShoppingSpreeItem or null if it does not exist.
     */
    @NonNull
    Optional<ShoppingSpreeItem> findById(@NonNull Long id);

    /**
     * Retrieves all ShoppingSpreeItems for a given ShoppingSpree.
     *
     * @param shoppingSpreeId The ID of the ShoppingSpree.
     * @return A List of ShoppingSpreeItems.
     */
    List<ShoppingSpreeItem> findAllByShoppingSpreeId(@NonNull Long shoppingSpreeId);
}
