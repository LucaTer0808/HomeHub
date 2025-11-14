package com.terfehr.homehub.application.event;

import com.terfehr.homehub.application.event.payload.DeleteShoppingListEventPayload;
import com.terfehr.homehub.domain.shared.exception.InvalidEventPayloadException;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class DeleteShoppingListEvent extends ApplicationEvent {

    private final DeleteShoppingListEventPayload payload;

    public DeleteShoppingListEvent(Object source, DeleteShoppingListEventPayload payload) throws InvalidEventPayloadException {
        super(source);
        if (payload == null) {
            throw new InvalidEventPayloadException("DeleteShoppingListEventPayload cannot be null");
        }
        this.payload = payload;
    }
}
