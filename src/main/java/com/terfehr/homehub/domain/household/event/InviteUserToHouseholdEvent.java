package com.terfehr.homehub.domain.household.event;

import com.terfehr.homehub.domain.household.event.payload.InviteUserToHouseholdEventPayload;
import com.terfehr.homehub.domain.shared.exception.InvalidDomainEventPayloadException;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class InviteUserToHouseholdEvent extends ApplicationEvent {

    private final InviteUserToHouseholdEventPayload payload;

    public InviteUserToHouseholdEvent(Object source, InviteUserToHouseholdEventPayload payload) {
        super(source);
        if (payload == null) {
            throw new InvalidDomainEventPayloadException("The given InviteUserToHouseholdEventPayload is null");
        }
        this.payload = payload;
    }
}
