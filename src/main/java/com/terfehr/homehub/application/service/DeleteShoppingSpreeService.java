package com.terfehr.homehub.application.service;

import com.terfehr.homehub.application.command.DeleteShoppingSpreeCommand;
import com.terfehr.homehub.application.event.DeleteShoppingSpreeEvent;
import com.terfehr.homehub.application.event.payload.DeleteShoppingSpreeEventPayload;
import com.terfehr.homehub.application.exception.ShoppingSpreeNotFoundException;
import com.terfehr.homehub.domain.bookkeeping.entity.Account;
import com.terfehr.homehub.domain.bookkeeping.entity.ShoppingExpense;
import com.terfehr.homehub.domain.household.entity.Household;
import com.terfehr.homehub.domain.household.repository.HouseholdRepositoryInterface;
import com.terfehr.homehub.domain.shopping.entity.ShoppingSpree;
import com.terfehr.homehub.domain.shopping.repository.ShoppingSpreeRepositoryInterface;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Transactional
public class DeleteShoppingSpreeService {

    private final ApplicationEventPublisher publisher;
    private final HouseholdRepositoryInterface householdRepository;
    private final ShoppingSpreeRepositoryInterface shoppingSpreeRepository;

    public void execute(DeleteShoppingSpreeCommand cmd) {
        ShoppingSpree spree = shoppingSpreeRepository.findById(cmd.id())
                .orElseThrow(() -> new ShoppingSpreeNotFoundException("Shopping spree not found"));

        Household household = spree.getHousehold();

        ShoppingExpense affectedExpense = spree.getShoppingExpense();

        Account account = affectedExpense.getAccount();

        household.deleteShoppingSpree(spree);
        account.removeTransaction(affectedExpense);

        householdRepository.save(household);

        DeleteShoppingSpreeEventPayload payload = new DeleteShoppingSpreeEventPayload(spree.getId(), spree.getDate());
        DeleteShoppingSpreeEvent event = new DeleteShoppingSpreeEvent(this, payload);
        publisher.publishEvent(event);
    }
}
