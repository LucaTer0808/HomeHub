package com.terfehr.homehub.domain.household.event;

import com.terfehr.homehub.domain.household.event.payload.DeleteHouseholdEventPayload;
import com.terfehr.homehub.domain.shared.exception.InvalidDomainEventPayloadException;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class DeleteHouseholdEvent extends ApplicationEvent {

    private final DeleteHouseholdEventPayload payload;

    public DeleteHouseholdEvent(Object source, DeleteHouseholdEventPayload payload) {
        super(source);
        if (payload == null) {
            throw new InvalidDomainEventPayloadException("DeleteHouseholdEventPayload cannot be null");
        }
        this.payload = payload;
    }
}
