package com.terfehr.homehub.domain.household.event;

import com.terfehr.homehub.domain.household.event.payload.ChangeEmailEventPayload;
import com.terfehr.homehub.domain.shared.exception.InvalidDomainEventPayloadException;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ChangeEmailEvent extends ApplicationEvent {

    private final ChangeEmailEventPayload eventPayload;

    public ChangeEmailEvent(Object source, ChangeEmailEventPayload eventPayload) throws InvalidDomainEventPayloadException {
        super(source);
        if (eventPayload == null) {
            throw new InvalidDomainEventPayloadException("Event payload cannot be null");
        }
        this.eventPayload = eventPayload;
    }
}
