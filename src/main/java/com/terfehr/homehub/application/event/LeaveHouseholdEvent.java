package com.terfehr.homehub.application.event;

import com.terfehr.homehub.application.event.payload.LeaveHouseholdEventPayload;
import com.terfehr.homehub.application.exception.InvalidApplicationEventPayloadException;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class LeaveHouseholdEvent extends ApplicationEvent {

    private final LeaveHouseholdEventPayload payload;

    public LeaveHouseholdEvent(Object object, LeaveHouseholdEventPayload payload) {
        super(object);
        if (payload == null) {
            throw new InvalidApplicationEventPayloadException("LeaveHouseholdEventPayload cannot be null");
        }
        this.payload = payload;
    }
}
