package com.terfehr.homehub.domain.bookkeeping.repository;

import com.terfehr.homehub.domain.bookkeeping.entity.Account;
import com.terfehr.homehub.domain.bookkeeping.entity.Transaction;
import com.terfehr.homehub.domain.household.entity.Roommate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    Set<Transaction> findByAccount(@NonNull Account account);

    /**
     * Retrieves all Transactions for a given Roommate.
     *
     * @param roommate The Roommate to get Transactions for.
     * @return A List of Transactions.
     */
    Set<Transaction> findAllByRoommate(@NonNull Roommate roommate);
}
