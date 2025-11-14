package com.terfehr.homehub.domain.household.event;

import com.terfehr.homehub.domain.household.event.payload.CreateAccountEventPayload;
import com.terfehr.homehub.domain.shared.exception.InvalidEventPayloadException;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CreateAccountEvent extends ApplicationEvent {

    private final CreateAccountEventPayload payload;

    public CreateAccountEvent(Object source, CreateAccountEventPayload payload) {
        super(source);
        if (payload == null) {
            throw new InvalidEventPayloadException("CreateAccountEventPayload cannot be null");
        }
        this.payload = payload;
    }
}
