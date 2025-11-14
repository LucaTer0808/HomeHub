package com.terfehr.homehub.application.event;

import com.terfehr.homehub.application.event.payload.ChangeAccountNameEventPayload;
import com.terfehr.homehub.domain.shared.exception.InvalidEventPayloadException;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ChangeAccountNameEvent extends ApplicationEvent {

    private final ChangeAccountNameEventPayload payload;

    public ChangeAccountNameEvent(Object source, ChangeAccountNameEventPayload payload) throws InvalidEventPayloadException {
        super(source);
        if (payload == null) {
            throw new InvalidEventPayloadException("ChangeAccountNameEventPayload cannot be null");
        }
        this.payload = payload;
    }
}
