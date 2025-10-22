package com.terfehr.homehub.domain.household.event;

import com.terfehr.homehub.domain.household.event.payload.CreateHouseholdEventPayload;
import com.terfehr.homehub.domain.shared.exception.InvalidDomainEventPayloadException;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CreateHouseholdEvent extends ApplicationEvent {

    private final CreateHouseholdEventPayload payload;

    public CreateHouseholdEvent(Object source, CreateHouseholdEventPayload payload) throws InvalidDomainEventPayloadException {
        super(source);
        if (payload == null) {
            throw new InvalidDomainEventPayloadException("CreateHouseholdEventPayload cannot be null");
        }
        this.payload = payload;
    }
}
