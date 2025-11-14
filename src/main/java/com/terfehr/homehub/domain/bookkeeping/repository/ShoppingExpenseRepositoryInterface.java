package com.terfehr.homehub.domain.bookkeeping.repository;

import com.terfehr.homehub.domain.bookkeeping.entity.ShoppingExpense;
import com.terfehr.homehub.domain.shopping.entity.ShoppingSpree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface ShoppingExpenseRepositoryInterface extends JpaRepository<ShoppingExpense, Long> {

    /**
     * Retrieves a ShoppingExpense by its ShoppingSpree.
     *
     * @param shoppingSpree The ShoppingSpree of the ShoppingExpense.
     * @return An Optional containing either the ShoppingExpense or null if it does not exist.
     */
    Optional<ShoppingExpense> findByShoppingSpree(@NonNull ShoppingSpree shoppingSpree);
}
