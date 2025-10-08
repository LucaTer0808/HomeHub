package com.terfehr.homehub.domain.bookkeeping.repository;

import com.terfehr.homehub.domain.bookkeeping.entity.Account;
import com.terfehr.homehub.domain.bookkeeping.entity.Expense;
import com.terfehr.homehub.domain.bookkeeping.entity.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

interface IncomeRepositoryInterface extends JpaRepository<Income, Long> {

    /**
     * Retrieves an Income by its id.
     *
     * @param id The ID of Income.
     * @return An Optional containing either the Income or null if it does not exist.
     */
    @NonNull
    Optional<Income> findById(@NonNull Long id);

    /**
     * Retrieves an Income by its Account.
     *
     * @param account The Account of the Income.
     * @return An Optional containing either the Income or null if it does not exist.
     */
    Optional<Income> findByAccount(Account account);
}
