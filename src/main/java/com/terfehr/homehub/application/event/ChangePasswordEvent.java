package com.terfehr.homehub.application.event;

import com.terfehr.homehub.application.event.payload.ChangePasswordEventPayload;
import com.terfehr.homehub.domain.shared.exception.InvalidEventPayloadException;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ChangePasswordEvent extends ApplicationEvent {

    private final ChangePasswordEventPayload payload;

    public ChangePasswordEvent(Object source, ChangePasswordEventPayload payload) throws InvalidEventPayloadException {
        super(source);
        if (payload == null) {
            throw new InvalidEventPayloadException("ChangePasswordEventPayload cannot be null");
        }
        this.payload = payload;
    }
}
