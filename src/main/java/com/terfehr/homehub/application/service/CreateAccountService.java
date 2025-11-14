package com.terfehr.homehub.application.service;

import com.terfehr.homehub.application.command.CreateAccountCommand;
import com.terfehr.homehub.application.dto.AccountDTO;
import com.terfehr.homehub.application.exception.HouseholdNotFoundException;
import com.terfehr.homehub.domain.bookkeeping.entity.Account;
import com.terfehr.homehub.domain.bookkeeping.exception.InvalidAccountNameException;
import com.terfehr.homehub.domain.bookkeeping.exception.InvalidCurrencyCodeException;
import com.terfehr.homehub.domain.household.entity.Household;
import com.terfehr.homehub.domain.household.event.CreateAccountEvent;
import com.terfehr.homehub.domain.household.event.payload.CreateAccountEventPayload;
import com.terfehr.homehub.domain.household.repository.HouseholdRepositoryInterface;
import com.terfehr.homehub.domain.shared.exception.InvalidEventPayloadException;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateAccountService {

    private final ApplicationEventPublisher publisher;
    private final HouseholdRepositoryInterface householdRepository;

    /**
     * Executed the given Command to create a new monetary account for a household.
     *
     * @param cmd The Command to execute.
     * @return The created Account as a DTO.
     * @throws HouseholdNotFoundException If the Household to add the Account to does not exist.
     * @throws InvalidAccountNameException If the given account name is invalid.
     * @throws InvalidCurrencyCodeException If the given currency code is invalid.
     * @throws InvalidEventPayloadException If the event payload is invalid.
     */
    public AccountDTO execute(CreateAccountCommand cmd) throws
            HouseholdNotFoundException,
            InvalidAccountNameException,
            InvalidCurrencyCodeException,
            InvalidEventPayloadException
    {
        Household household = householdRepository.findById(cmd.id())
                .orElseThrow(() -> new HouseholdNotFoundException("Household not found for ID: " + cmd.id()));

        Account account = household.addAccount(cmd.name(), cmd.amount(), cmd.currencyCode());

        householdRepository.save(household); // due to cascadeType.ALL, the account is saved as well.

        CreateAccountEventPayload payload = new CreateAccountEventPayload(account.getId(), account.getName(), account.getBalance().withSymbol());
        CreateAccountEvent event = new CreateAccountEvent(this, payload);
        publisher.publishEvent(event);

        return new AccountDTO(account.getId(), account.getName(), account.getBalance().withSymbol());
    }
}
