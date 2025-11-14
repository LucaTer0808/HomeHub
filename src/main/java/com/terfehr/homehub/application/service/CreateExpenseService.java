package com.terfehr.homehub.application.service;

import com.terfehr.homehub.application.command.CreateExpenseCommand;
import com.terfehr.homehub.application.dto.ExpenseDTO;
import com.terfehr.homehub.application.event.CreateExpenseEvent;
import com.terfehr.homehub.application.event.payload.CreateExpenseEventPayload;
import com.terfehr.homehub.application.exception.AccountNotFoundException;
import com.terfehr.homehub.application.exception.RoommateNotFoundException;
import com.terfehr.homehub.application.interfaces.AuthUserProviderInterface;
import com.terfehr.homehub.domain.bookkeeping.entity.Account;
import com.terfehr.homehub.domain.bookkeeping.entity.Expense;
import com.terfehr.homehub.domain.bookkeeping.repository.AccountRepositoryInterface;
import com.terfehr.homehub.domain.household.entity.Roommate;
import com.terfehr.homehub.domain.household.entity.User;
import com.terfehr.homehub.domain.household.repository.RoommateRepositoryInterface;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateExpenseService {

    private final AccountRepositoryInterface accountRepository;
    private final ApplicationEventPublisher publisher;
    private final RoommateRepositoryInterface roommateRepository;
    private final AuthUserProviderInterface userProvider;

    public ExpenseDTO execute(CreateExpenseCommand cmd) {
        User user = userProvider.getUser();

        Account account = accountRepository.findById(cmd.accountId())
                .orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + cmd.accountId()));

        Roommate roommate = roommateRepository.findByHouseholdIdAndUserEmail(account.getHousehold().getId(), user.getEmail())
                .orElseThrow(() -> new RoommateNotFoundException("Account not found with ID: " + cmd.accountId()));

        Expense expense = account.createExpense(cmd.amount(), cmd.description(), cmd.date(), cmd.recipient(), roommate);

        accountRepository.save(account);

        CreateExpenseEventPayload payload = new CreateExpenseEventPayload(
                expense.getId(),
                expense.getAmount().getAmountInSmallestUnit(),
                expense.getDescription(),
                expense.getDate(),
                expense.getRecipient()
        );
        CreateExpenseEvent event = new CreateExpenseEvent(this, payload);
        publisher.publishEvent(event);

        return new ExpenseDTO(
                expense.getId(),
                expense.getAmount().getAmountInSmallestUnit(),
                expense.getAmount().withSymbol(),
                expense.getDescription(),
                expense.getDate(),
                expense.getRecipient()
        );
    }
}
