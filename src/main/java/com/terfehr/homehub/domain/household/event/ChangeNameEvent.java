package com.terfehr.homehub.domain.household.event;

import com.terfehr.homehub.domain.household.event.payload.ChangeNameEventPayload;
import com.terfehr.homehub.domain.shared.exception.InvalidEventPayloadException;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ChangeNameEvent extends ApplicationEvent {

    private final ChangeNameEventPayload payload;

    public ChangeNameEvent(Object source, ChangeNameEventPayload payload) {
        super(source);
        if (payload == null) {
            throw new InvalidEventPayloadException("ChangeNameEventPayload cannot be null");
        }
        this.payload = payload;
    }
}
