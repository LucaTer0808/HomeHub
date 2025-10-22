package com.terfehr.homehub.domain.bookkeeping.service;

import com.terfehr.homehub.domain.bookkeeping.entity.Transaction;
import com.terfehr.homehub.domain.bookkeeping.repository.TransactionRepositoryInterface;
import com.terfehr.homehub.domain.household.entity.Roommate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TransactionService {

    private final TransactionRepositoryInterface transactionRepository;

    /**
     * Removes the given roommates from all transactions by fetching them from the Database and setting them to null.
     * @param roommates The roommates to remove from all transactions.
     */
    public void setNullByRoommates(Set<Roommate> roommates) {
        Set<Transaction> alteredTransactions = roommates.stream()
                .flatMap(roommate -> transactionRepository.findAllByRoommate(roommate).stream())
                .peek(Transaction::removeRoommate)
                .collect(Collectors.toSet());

        transactionRepository.saveAll(alteredTransactions);
    }

}
