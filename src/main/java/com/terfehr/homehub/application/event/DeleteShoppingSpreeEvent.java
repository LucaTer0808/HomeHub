package com.terfehr.homehub.application.event;

import com.terfehr.homehub.application.event.payload.DeleteShoppingSpreeEventPayload;
import com.terfehr.homehub.domain.shared.exception.InvalidEventPayloadException;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class DeleteShoppingSpreeEvent extends ApplicationEvent {

    private final DeleteShoppingSpreeEventPayload payload;

    public DeleteShoppingSpreeEvent(Object source, DeleteShoppingSpreeEventPayload payload) {
        super(source);
        if (payload == null) {
            throw new InvalidEventPayloadException("DeleteShoppingSpreeEventPayload cannot be null");
        }
        this.payload = payload;
    }
}
