package com.terfehr.homehub.application.event;

import com.terfehr.homehub.application.event.payload.InviteUserToHouseholdEventPayload;
import com.terfehr.homehub.domain.shared.exception.InvalidEventPayloadException;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class InviteUserToHouseholdEvent extends ApplicationEvent {

    private final InviteUserToHouseholdEventPayload payload;

    public InviteUserToHouseholdEvent(Object source, InviteUserToHouseholdEventPayload payload) {
        super(source);
        if (payload == null) {
            throw new InvalidEventPayloadException("The given InviteUserToHouseholdEventPayload is null");
        }
        this.payload = payload;
    }
}
