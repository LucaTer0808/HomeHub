package com.terfehr.homehub.application.event;

import com.terfehr.homehub.application.event.payload.ChangeEmailEventPayload;
import com.terfehr.homehub.domain.shared.exception.InvalidEventPayloadException;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ChangeEmailEvent extends ApplicationEvent {

    private final ChangeEmailEventPayload eventPayload;

    public ChangeEmailEvent(Object source, ChangeEmailEventPayload eventPayload) throws InvalidEventPayloadException {
        super(source);
        if (eventPayload == null) {
            throw new InvalidEventPayloadException("Event payload cannot be null");
        }
        this.eventPayload = eventPayload;
    }
}
