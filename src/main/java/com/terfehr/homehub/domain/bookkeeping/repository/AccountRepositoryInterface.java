package com.terfehr.homehub.domain.bookkeeping.repository;

import com.terfehr.homehub.domain.bookkeeping.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface AccountRepositoryInterface extends JpaRepository<Account, Long> {

    /**
     * Retrieves an Account by its ID.
     *
     * @param id The ID of the Account.
     * @return An Optional containing either the Account or Null if there is none.
     */
    @NonNull
    Optional<Account> findById(@NonNull Long id);
}
