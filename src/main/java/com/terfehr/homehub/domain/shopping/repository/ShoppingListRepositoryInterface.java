package com.terfehr.homehub.domain.shopping.repository;

import com.terfehr.homehub.domain.shopping.entity.ShoppingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface ShoppingListRepositoryInterface extends JpaRepository<ShoppingList, Long> {

    /**
     * Retrieves a ShoppingList by its ID.
     *
     * @param id The ID of the ShoppingList.
     * @return An Optional containing either the ShoppingList or null if it does not exist.
     */
    @NonNull
    Optional<ShoppingList> findById(@NonNull Long id);

    /**
     * Retrieves a ShoppingList by its name.
     *
     * @param name The name of the ShoppingList.
     * @return An Optional containing either the ShoppingList or null if it does not exist.
     */
    @NonNull
    Optional<ShoppingList> findByName(String name);
}
