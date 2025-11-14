package com.terfehr.homehub.application.event;

import com.terfehr.homehub.application.event.payload.CreateExpenseEventPayload;
import com.terfehr.homehub.domain.shared.exception.InvalidEventPayloadException;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CreateExpenseEvent extends ApplicationEvent {

    private final CreateExpenseEventPayload payload;

    public CreateExpenseEvent(Object source, CreateExpenseEventPayload payload) throws InvalidEventPayloadException {
        super(source);
        if (payload == null) {
            throw new InvalidEventPayloadException("CreateExpenseEventPayload cannot be null");
        }
        this.payload = payload;
    }
}
