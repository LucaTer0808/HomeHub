package com.terfehr.homehub.domain.household.event;

import com.terfehr.homehub.domain.household.event.payload.ChangeUsernameEventPayload;
import com.terfehr.homehub.domain.shared.exception.InvalidEventPayloadException;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ChangeUsernameEvent extends ApplicationEvent {

    private final ChangeUsernameEventPayload payload;

    public ChangeUsernameEvent(Object source, ChangeUsernameEventPayload payload) throws InvalidEventPayloadException {
        super(source);
        if (payload == null) {
            throw new InvalidEventPayloadException("ChangeUsernameEventPayload cannot be null");
        }
        this.payload = payload;
    }
}
