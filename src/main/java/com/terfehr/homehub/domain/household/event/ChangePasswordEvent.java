package com.terfehr.homehub.domain.household.event;

import com.terfehr.homehub.domain.household.event.payload.ChangePasswordEventPayload;
import com.terfehr.homehub.domain.shared.exception.InvalidDomainEventPayloadException;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ChangePasswordEvent extends ApplicationEvent {

    private final ChangePasswordEventPayload payload;

    public ChangePasswordEvent(Object source, ChangePasswordEventPayload payload) throws InvalidDomainEventPayloadException {
        super(source);
        if (payload == null) {
            throw new InvalidDomainEventPayloadException("ChangePasswordEventPayload cannot be null");
        }
        this.payload = payload;
    }
}
