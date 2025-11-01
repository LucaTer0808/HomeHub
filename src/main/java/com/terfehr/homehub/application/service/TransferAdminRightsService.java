package com.terfehr.homehub.application.service;

import com.terfehr.homehub.application.command.TransferAdminRightsCommand;
import com.terfehr.homehub.application.dto.RoommateDTO;
import com.terfehr.homehub.application.exception.RoommateNotFoundException;
import com.terfehr.homehub.application.interfaces.AuthUserProviderInterface;
import com.terfehr.homehub.domain.household.entity.Roommate;
import com.terfehr.homehub.domain.household.entity.User;
import com.terfehr.homehub.domain.household.event.TransferAdminRightsEvent;
import com.terfehr.homehub.domain.household.event.payload.TransferAdminRightsEventPayload;
import com.terfehr.homehub.domain.household.repository.RoommateRepositoryInterface;
import com.terfehr.homehub.domain.household.service.HouseholdService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TransferAdminRightsService {

    private final ApplicationEventPublisher publisher;
    private final AuthUserProviderInterface userProvider;
    private final HouseholdService householdService;
    private final RoommateRepositoryInterface roommateRepository;

    public RoommateDTO execute(TransferAdminRightsCommand cmd) {
        User user = userProvider.getUser();
        Roommate oldAdmin = roommateRepository.findByHouseholdIdAndUserEmail(cmd.id(), user.getEmail())
                .orElseThrow(() -> new RoommateNotFoundException("There is no Roommate connecting the Household with ID: " + cmd.id() + " to the User with Email: " + user.getEmail()));
        Roommate newAdmin = roommateRepository.findByHouseholdIdAndUserEmail(cmd.id(), cmd.email())
                .orElseThrow(() -> new RoommateNotFoundException("There is no Roommate connecting the Household with ID: " + cmd.id() + " to the User with Email: " + cmd.email()));

        householdService.transferAdminRights(oldAdmin, newAdmin);
        roommateRepository.save(oldAdmin);
        roommateRepository.save(newAdmin);

        TransferAdminRightsEventPayload payload = new TransferAdminRightsEventPayload(newAdmin.getHousehold().getId(), newAdmin.getUser().getEmail(), newAdmin.getRole().getValue());
        TransferAdminRightsEvent event = new TransferAdminRightsEvent(this, payload);
        publisher.publishEvent(event);

        return new RoommateDTO(newAdmin.getHousehold().getId(), newAdmin.getUser().getEmail(), newAdmin.getRole().getValue());
    }
}
