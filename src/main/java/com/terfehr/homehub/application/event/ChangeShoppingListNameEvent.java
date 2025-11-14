package com.terfehr.homehub.application.event;

import com.terfehr.homehub.application.event.payload.ChangeShoppingListNameEventPayload;
import com.terfehr.homehub.domain.shared.exception.InvalidEventPayloadException;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ChangeShoppingListNameEvent extends ApplicationEvent {

    private final ChangeShoppingListNameEventPayload payload;

    public ChangeShoppingListNameEvent(Object source, ChangeShoppingListNameEventPayload payload) {
        super(source);
        if (payload == null) {
            throw new InvalidEventPayloadException("ChangeShoppingListNameEventPayload cannot be null");
        }
        this.payload = payload;
    }
}
