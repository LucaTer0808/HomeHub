package com.terfehr.homehub.domain.household.event;

import com.terfehr.homehub.domain.household.event.payload.VerifyEmailChangeEventPayload;
import com.terfehr.homehub.domain.shared.exception.InvalidEventPayloadException;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class VerifyEmailChangeEvent extends ApplicationEvent {

    private final VerifyEmailChangeEventPayload eventPayload;

    public VerifyEmailChangeEvent(Object source, VerifyEmailChangeEventPayload eventPayload) {
        super(source);
        if (eventPayload == null) {
            throw new InvalidEventPayloadException("Event payload cannot be null");
        }
        this.eventPayload = eventPayload;
    }
}
