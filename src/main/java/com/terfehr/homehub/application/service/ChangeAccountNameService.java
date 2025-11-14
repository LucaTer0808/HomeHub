package com.terfehr.homehub.application.service;

import com.terfehr.homehub.application.command.ChangeAccountNameCommand;
import com.terfehr.homehub.application.dto.AccountDTO;
import com.terfehr.homehub.application.event.ChangeAccountNameEvent;
import com.terfehr.homehub.application.event.payload.ChangeAccountNameEventPayload;
import com.terfehr.homehub.application.exception.AccountNotFoundException;
import com.terfehr.homehub.domain.bookkeeping.entity.Account;
import com.terfehr.homehub.domain.bookkeeping.repository.AccountRepositoryInterface;
import com.terfehr.homehub.domain.shared.exception.InvalidAccountNameException;
import com.terfehr.homehub.domain.shared.exception.InvalidEventPayloadException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Transactional
public class ChangeAccountNameService {

    private final ApplicationEventPublisher publisher;
    private final AccountRepositoryInterface accountRepository;

    /**
     * Executes the command to change the name of an account. It fetches the account from the repository,
     * changes the name, saves the updated account, and publishes a ChangeAccountNameEvent.
     *
     * @param cmd The Command to execute.
     * @return The updated Account as a DTO.
     * @throws AccountNotFoundException If the Account to update does not exist.
     * @throws InvalidAccountNameException If the given account name is invalid.
     * @throws InvalidEventPayloadException If the event payload is invalid. Should not happen
     */
    public AccountDTO execute(ChangeAccountNameCommand cmd) throws
            AccountNotFoundException,
            InvalidAccountNameException,
            InvalidEventPayloadException
    {
        Account account = accountRepository.findById(cmd.id())
                .orElseThrow(() -> new AccountNotFoundException("There is no Account with the given ID: " + cmd.id()));

        account.changeName(cmd.name());

        accountRepository.save(account);

        ChangeAccountNameEventPayload payload = new ChangeAccountNameEventPayload(account.getId(), account.getName());
        ChangeAccountNameEvent event = new ChangeAccountNameEvent(this, payload);
        publisher.publishEvent(event);

        return new AccountDTO(account.getId(), account.getName(), account.getBalance().getAmountInSmallestUnit(), account.getBalance().withSymbol());
    }
}
