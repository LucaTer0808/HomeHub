package com.terfehr.homehub.domain.household.event;

import com.terfehr.homehub.domain.household.event.payload.JoinHouseholdEventPayload;
import com.terfehr.homehub.domain.shared.exception.InvalidEventPayloadException;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class JoinHouseholdEvent extends ApplicationEvent {

    private final JoinHouseholdEventPayload payload;

    public JoinHouseholdEvent(Object source, JoinHouseholdEventPayload payload) throws InvalidEventPayloadException {
        super(source);
        if (payload == null) {
            throw new InvalidEventPayloadException("JoinHouseholdEventPayload cannot be null");
        }
        this.payload = payload;
    }
}
