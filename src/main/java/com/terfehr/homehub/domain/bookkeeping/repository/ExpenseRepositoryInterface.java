package com.terfehr.homehub.domain.bookkeeping.repository;

import com.terfehr.homehub.domain.bookkeeping.entity.Account;
import com.terfehr.homehub.domain.bookkeeping.entity.Expense;
import com.terfehr.homehub.domain.household.entity.Roommate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;
import java.util.Set;

public interface ExpenseRepositoryInterface extends JpaRepository<Expense, Long> {

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

    /**
     * Get all Expenses for a given Roommate.
     *
     * @param roommate The Roommate to get Expenses for.
     * @return A Set of Expenses.
     */
    Set<Expense> findAllByRoommate(Roommate roommate);

}
