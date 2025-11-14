package com.terfehr.homehub.application.service;

import com.terfehr.homehub.application.command.DeleteAccountCommand;
import com.terfehr.homehub.application.event.DeleteAccountEvent;
import com.terfehr.homehub.application.event.payload.DeleteAccountEventPayload;
import com.terfehr.homehub.application.exception.AccountNotFoundException;
import com.terfehr.homehub.application.exception.HouseholdNotFoundException;
import com.terfehr.homehub.domain.bookkeeping.entity.Account;
import com.terfehr.homehub.domain.bookkeeping.repository.AccountRepositoryInterface;
import com.terfehr.homehub.domain.household.entity.Household;
import com.terfehr.homehub.domain.household.repository.HouseholdRepositoryInterface;
import com.terfehr.homehub.domain.shared.exception.InvalidAccountException;
import com.terfehr.homehub.domain.shared.exception.InvalidEventPayloadException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Transactional
public class DeleteAccountService {

    private final ApplicationEventPublisher publisher;
    private final AccountRepositoryInterface accountRepository;
    private final HouseholdRepositoryInterface householdRepository;

    /**
     * Executed the given Command to delete an Account from a Household.
     *
     * @param cmd The Command to execute.
     * @throws HouseholdNotFoundException If the Household to delete the Account from does not exist.
     * @throws AccountNotFoundException If the Account to delete does not exist.
     * @throws InvalidAccountException If the provided Account is invalid or not associated with this household.
     * @throws InvalidEventPayloadException If the event payload is invalid.
     */
    public void execute(DeleteAccountCommand cmd) throws
            HouseholdNotFoundException,
            AccountNotFoundException,
            InvalidAccountException,
            InvalidEventPayloadException
    {
        Account account = accountRepository.findById(cmd.accountId())
                .orElseThrow(() -> new AccountNotFoundException("There is no Account with the ID: " + cmd.accountId()));

        Household household = householdRepository.findById(account.getHousehold().getId())
                .orElseThrow(() -> new HouseholdNotFoundException("There is no Household with the ID: " + account.getHousehold().getId()));

        household.deleteAccount(account);
        householdRepository.save(household);

        DeleteAccountEventPayload payload = new DeleteAccountEventPayload(household.getId(), account.getId(), account.getName(), account.getBalance().withCurrencyCode());
        DeleteAccountEvent event = new DeleteAccountEvent(this, payload);
        publisher.publishEvent(event);
    }
}
