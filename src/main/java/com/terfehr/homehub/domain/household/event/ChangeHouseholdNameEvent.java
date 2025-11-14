package com.terfehr.homehub.domain.household.event;

import com.terfehr.homehub.domain.household.event.payload.ChangeHouseholdNameEventPayload;
import com.terfehr.homehub.domain.shared.exception.InvalidEventPayloadException;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ChangeHouseholdNameEvent extends ApplicationEvent {

    private final ChangeHouseholdNameEventPayload payload;

    public ChangeHouseholdNameEvent(Object source, ChangeHouseholdNameEventPayload payload) {
        super(source);
        if (payload == null) {
            throw new InvalidEventPayloadException("ChangeHouseholdNameEventPayload cannot be null");
        }
        this.payload = payload;
    }
}
