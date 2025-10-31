package com.terfehr.homehub.domain.bookkeeping.service;

import com.terfehr.homehub.domain.bookkeeping.entity.Transaction;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class TransactionService {

    /**
     * Removes the Roommate for all given transactions.
     *
     * @param transactions The set of transactions to remove the Roommate from.
     */
    public void removeRoommates(Set<Transaction> transactions) {
        transactions.forEach(Transaction::removeRoommate);
    }


}
