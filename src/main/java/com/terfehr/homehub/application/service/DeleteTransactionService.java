package com.terfehr.homehub.application.service;

import com.terfehr.homehub.application.command.DeleteTransactionCommand;
import com.terfehr.homehub.application.event.DeleteTransactionEvent;
import com.terfehr.homehub.application.event.payload.DeleteTransactionEventPayload;
import com.terfehr.homehub.application.exception.TransactionNotFoundException;
import com.terfehr.homehub.domain.bookkeeping.entity.Account;
import com.terfehr.homehub.domain.bookkeeping.entity.ShoppingExpense;
import com.terfehr.homehub.domain.bookkeeping.entity.Transaction;
import com.terfehr.homehub.domain.bookkeeping.repository.AccountRepositoryInterface;
import com.terfehr.homehub.domain.bookkeeping.repository.TransactionRepositoryInterface;
import com.terfehr.homehub.domain.shopping.entity.ShoppingSpree;
import com.terfehr.homehub.domain.shopping.repository.ShoppingSpreeRepositoryInterface;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Transactional
public class DeleteTransactionService {

    private final AccountRepositoryInterface accountRepository;
    private final ApplicationEventPublisher publisher;
    private final ShoppingSpreeRepositoryInterface shoppingSpreeRepository;
    private final TransactionRepositoryInterface transactionRepository;

    /**
     * Executes the given command to delete a transaction. If removes the Transaction from its Account. If the Transaction is a ShoppingExpense,
     * the Reference of its ShoppingSpree to said Expense is removed as well. Finally, an Event is published notifying about the deletion.
     *
     * @param cmd The Command to execute.
     * @throws TransactionNotFoundException If the Transaction to delete does not exist.
     */
    public void execute(DeleteTransactionCommand cmd) throws TransactionNotFoundException {
        Transaction transaction = transactionRepository.findById(cmd.transactionId())
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with ID: " + cmd.transactionId()));

        Account account = transaction.getAccount();

        if (transaction instanceof ShoppingExpense) {
            ShoppingSpree spree = ((ShoppingExpense) transaction).getShoppingSpree();

            if (spree != null) {
                spree.removeShoppingExpense();
                shoppingSpreeRepository.save(spree);
            }
        }

        account.deleteTransaction(transaction);

        accountRepository.save(account);

        DeleteTransactionEventPayload payload = new DeleteTransactionEventPayload(transaction.getId(), transaction.getAmount().getAmountInSmallestUnit(), transaction.getDescription(), transaction.getDate());
        DeleteTransactionEvent event = new DeleteTransactionEvent(this, payload);
        publisher.publishEvent(event);
    }
}
