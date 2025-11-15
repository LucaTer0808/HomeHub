package com.terfehr.homehub.application.service;

import com.terfehr.homehub.application.command.CreateIncomeCommand;
import com.terfehr.homehub.application.dto.ExpenseDTO;
import com.terfehr.homehub.application.dto.IncomeDTO;
import com.terfehr.homehub.application.event.CreateExpenseEvent;
import com.terfehr.homehub.application.event.CreateIncomeEvent;
import com.terfehr.homehub.application.event.payload.CreateExpenseEventPayload;
import com.terfehr.homehub.application.event.payload.CreateIncomeEventPayload;
import com.terfehr.homehub.application.exception.AccountNotFoundException;
import com.terfehr.homehub.application.exception.RoommateNotFoundException;
import com.terfehr.homehub.application.exception.UserNotFoundException;
import com.terfehr.homehub.application.interfaces.AuthUserProviderInterface;
import com.terfehr.homehub.domain.bookkeeping.entity.Account;
import com.terfehr.homehub.domain.bookkeeping.entity.Expense;
import com.terfehr.homehub.domain.bookkeeping.entity.Income;
import com.terfehr.homehub.domain.bookkeeping.repository.AccountRepositoryInterface;
import com.terfehr.homehub.domain.household.entity.Roommate;
import com.terfehr.homehub.domain.household.entity.User;
import com.terfehr.homehub.domain.household.repository.RoommateRepositoryInterface;
import com.terfehr.homehub.domain.shared.exception.InvalidAmountException;
import com.terfehr.homehub.domain.shared.exception.InvalidDateException;
import com.terfehr.homehub.domain.shared.exception.InvalidDescriptionException;
import com.terfehr.homehub.domain.shared.exception.InvalidSourceException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Transactional
public class CreateIncomeService {

    private final ApplicationEventPublisher publisher;
    private final AuthUserProviderInterface userProvider;
    private final AccountRepositoryInterface accountRepository;
    private final RoommateRepositoryInterface roommateRepository;

    public IncomeDTO execute(CreateIncomeCommand cmd) throws
            AuthenticationCredentialsNotFoundException,
            UserNotFoundException,
            AccountNotFoundException,
            RoommateNotFoundException,
            InvalidAmountException,
            InvalidDescriptionException,
            InvalidDateException,
            InvalidSourceException
    {
        User user = userProvider.getUser();

        Account account = accountRepository.findById(cmd.accountId())
                .orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + cmd.accountId()));

        Roommate roommate = roommateRepository.findByHouseholdIdAndUserEmail(account.getHousehold().getId(), user.getEmail())
                .orElseThrow(() -> new RoommateNotFoundException("Account not found with ID: " + cmd.accountId()));

        Income income = account.createIncome(cmd.amount(), cmd.description(), cmd.date(), cmd.source(), roommate);

        accountRepository.save(account);

        CreateIncomeEventPayload payload = new CreateIncomeEventPayload(
                income.getId(),
                income.getAmount().getAmountInSmallestUnit(),
                income.getDescription(),
                income.getDate(),
                income.getSource()
        );
        CreateIncomeEvent event = new CreateIncomeEvent(this, payload);
        publisher.publishEvent(event);

        return new IncomeDTO(
                income.getId(),
                income.getAmount().getAmountInSmallestUnit(),
                income.getAmount().withSymbol(),
                income.getDescription(),
                income.getDate(),
                income.getSource()
        );
    }
}
