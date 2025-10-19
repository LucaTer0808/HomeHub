package com.terfehr.homehub.domain.household.event;

import com.terfehr.homehub.domain.household.event.payload.ResetPasswordEventPayload;
import com.terfehr.homehub.domain.shared.exception.InvalidEventPayloadException;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ResetPasswordEvent extends ApplicationEvent {

    private final ResetPasswordEventPayload eventPayload;

    public ResetPasswordEvent(Object source, ResetPasswordEventPayload eventPayload) {
        super(source);
        if (eventPayload == null) {
            throw new InvalidEventPayloadException("ResetPasswordEventPayload cannot be null");
        }
        this.eventPayload = eventPayload;
    }
}
