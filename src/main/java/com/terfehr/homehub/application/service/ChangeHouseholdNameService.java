package com.terfehr.homehub.application.service;

import com.terfehr.homehub.application.command.ChangeHouseholdNameCommand;
import com.terfehr.homehub.application.dto.HouseholdDTO;
import com.terfehr.homehub.application.exception.HouseholdNotFoundException;
import com.terfehr.homehub.domain.household.entity.Household;
import com.terfehr.homehub.application.event.ChangeHouseholdNameEvent;
import com.terfehr.homehub.application.event.payload.ChangeHouseholdNameEventPayload;
import com.terfehr.homehub.domain.household.repository.HouseholdRepositoryInterface;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Transactional
public class ChangeHouseholdNameService {

    private final ApplicationEventPublisher publisher;
    private final HouseholdRepositoryInterface householdRepository;

    public HouseholdDTO execute(ChangeHouseholdNameCommand cmd) {

        Household household = householdRepository.findById(cmd.id())
                .orElseThrow(() -> new HouseholdNotFoundException("Household not found with ID: " + cmd.id()));

        household.changeName(cmd.name());

        householdRepository.save(household);

        ChangeHouseholdNameEventPayload payload = new ChangeHouseholdNameEventPayload(cmd.id(), cmd.name());
        ChangeHouseholdNameEvent event = new ChangeHouseholdNameEvent(this, payload);
        publisher.publishEvent(event);

        return new HouseholdDTO(household.getId(), household.getName());
    }
}
