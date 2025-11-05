package com.terfehr.homehub.application.service;

import com.terfehr.homehub.application.command.TransferAdminRightsCommand;
import com.terfehr.homehub.application.dto.RoommateDTO;
import com.terfehr.homehub.application.exception.HouseholdNotFoundException;
import com.terfehr.homehub.application.exception.RoommateNotFoundException;
import com.terfehr.homehub.application.interfaces.AuthUserProviderInterface;
import com.terfehr.homehub.domain.household.entity.Household;
import com.terfehr.homehub.domain.household.entity.Roommate;
import com.terfehr.homehub.domain.household.entity.User;
import com.terfehr.homehub.domain.household.event.TransferAdminRightsEvent;
import com.terfehr.homehub.domain.household.event.payload.TransferAdminRightsEventPayload;
import com.terfehr.homehub.domain.household.repository.HouseholdRepositoryInterface;
import com.terfehr.homehub.domain.household.repository.RoommateRepositoryInterface;
import com.terfehr.homehub.domain.household.service.HouseholdService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TransferAdminRightsService {

    private final ApplicationEventPublisher publisher;
    private final HouseholdRepositoryInterface householdRepository;
    private final RoommateRepositoryInterface roommateRepository;

    public RoommateDTO execute(TransferAdminRightsCommand cmd) {
        Roommate newAdmin = roommateRepository.findByHouseholdIdAndUserEmail(cmd.id(), cmd.email())
                .orElseThrow(() -> new RoommateNotFoundException("There is no Roommate connecting the Household with ID: " + cmd.id() + " to the User with Email: " + cmd.email()));
        Household household = householdRepository.findById(cmd.id())
                        .orElseThrow(() -> new HouseholdNotFoundException("There is no Household with ID: " + cmd.id()));

        household.transferAdminRights(newAdmin);
        householdRepository.save(household);

        TransferAdminRightsEventPayload payload = new TransferAdminRightsEventPayload(newAdmin.getHousehold().getId(), newAdmin.getUser().getEmail(), newAdmin.getRole().getValue());
        TransferAdminRightsEvent event = new TransferAdminRightsEvent(this, payload);
        publisher.publishEvent(event);

        return new RoommateDTO(newAdmin.getHousehold().getId(), newAdmin.getUser().getEmail(), newAdmin.getRole().getValue());
    }
}
