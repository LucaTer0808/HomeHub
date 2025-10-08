package com.terfehr.homehub.domain.shopping.repository;

import com.terfehr.homehub.domain.shopping.entity.ShoppingSpree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface ShoppingSpreeRepositoryInterface extends JpaRepository<ShoppingSpree, Long> {

    /**
     * Retrieves a ShoppingSpree by its ID.
     *
     * @param id The ID of the ShoppingSpree.
     * @return An Optional containing either the ShoppingSpree or null if it does not exist.
     */
    @NonNull
    Optional<ShoppingSpree> findById(@NonNull Long id);
}
