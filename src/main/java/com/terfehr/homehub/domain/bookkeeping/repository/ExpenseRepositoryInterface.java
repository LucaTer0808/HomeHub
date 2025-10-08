package com.terfehr.homehub.domain.bookkeeping.repository;

import com.terfehr.homehub.domain.bookkeeping.entity.Account;
import com.terfehr.homehub.domain.bookkeeping.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

interface ExpenseRepositoryInterface extends JpaRepository<Expense, Long> {

    /**
     * Retrieves an Expense by its id.
     *
     * @param id The ID of Expense.
     * @return An Optional containing either the Expense or null if it does not exist.
     */
    @NonNull
    Optional<Expense> findById(@NonNull Long id);

    /**
     * Retrieves an Expense by its Account.
     *
     * @param account The Account of the Expense.
     * @return An Optional containing either the Expense or null if it does not exist.
     */
    Optional<Expense> findByAccount(Account account);
}
