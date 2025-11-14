package com.terfehr.homehub.application.event;

import com.terfehr.homehub.application.event.payload.CreateShoppingListEventPayload;
import com.terfehr.homehub.domain.shared.exception.InvalidEventPayloadException;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CreateShoppingListEvent extends ApplicationEvent {

    private final CreateShoppingListEventPayload payload;

    public CreateShoppingListEvent(Object source, CreateShoppingListEventPayload payload) throws InvalidEventPayloadException {
        super(source);
        if (payload == null) {
            throw new InvalidEventPayloadException("CreateShoppingListEventPayload cannot be null");
        }
        this.payload = payload;
    }
}
