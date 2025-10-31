package com.terfehr.homehub.domain.household.event;

import com.terfehr.homehub.domain.household.event.payload.JoinHouseholdEventPayload;
import com.terfehr.homehub.domain.shared.exception.InvalidDomainEventPayloadException;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class JoinHouseholdEvent extends ApplicationEvent {

    private final JoinHouseholdEventPayload payload;

    public JoinHouseholdEvent(Object source, JoinHouseholdEventPayload payload) throws InvalidDomainEventPayloadException{
        super(source);
        if (payload == null) {
            throw new InvalidDomainEventPayloadException("JoinHouseholdEventPayload cannot be null");
        }
        this.payload = payload;
    }
}
