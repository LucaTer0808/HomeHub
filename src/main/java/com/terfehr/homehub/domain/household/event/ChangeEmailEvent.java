package com.terfehr.homehub.domain.household.event;

import com.terfehr.homehub.domain.household.event.payload.ChangeEmailEventPayload;
import com.terfehr.homehub.domain.shared.exception.InvalidEventPayloadException;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ChangeEmailEvent extends ApplicationEvent {

    private final ChangeEmailEventPayload eventPayload;

    public ChangeEmailEvent(Object source, ChangeEmailEventPayload eventPayload) {
        super(source);
        if (eventPayload == null) {
            throw new InvalidEventPayloadException("Event payload cannot be null");
        }d
        this.eventPayload = eventPayload;
    }
}
