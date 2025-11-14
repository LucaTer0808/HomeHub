package com.terfehr.homehub.application.event;

import com.terfehr.homehub.application.event.payload.LeaveHouseholdEventPayload;
import com.terfehr.homehub.domain.shared.exception.InvalidEventPayloadException;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class LeaveHouseholdEvent extends ApplicationEvent {

    private final LeaveHouseholdEventPayload payload;

    public LeaveHouseholdEvent(Object object, LeaveHouseholdEventPayload payload) throws InvalidEventPayloadException {
        super(object);
        if (payload == null) {
            throw new InvalidEventPayloadException("LeaveHouseholdEventPayload cannot be null");
        }
        this.payload = payload;
    }
}
