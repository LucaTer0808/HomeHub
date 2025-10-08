package com.terfehr.homehub.domain.bookkeeping.repository;

import com.terfehr.homehub.domain.bookkeeping.entity.Account;
import com.terfehr.homehub.domain.bookkeeping.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface TransactionRepositoryInterface extends JpaRepository<Transaction, Long> {

    /**
     * Retrieves a Transaction by its ID.
     *
     * @param id The ID of Transaction.
     * @return An Optional containing either the Transaction or null if it does not exist.
     */
    @NonNull
    Optional<Transaction> findById(@NonNull Long id);

    /**
     * Retrieves a Transaction by its Account.
     *
     * @param account The Account of the Transaction.
     * @return An Optional containing either the Transaction or null if it does not exist.
     */
    List<Transaction> findByAccount(@NonNull Account account);
}
