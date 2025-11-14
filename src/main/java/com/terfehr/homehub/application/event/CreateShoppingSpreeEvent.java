package com.terfehr.homehub.application.event;

import com.terfehr.homehub.application.event.payload.CreateShoppingSpreeEventPayload;
import com.terfehr.homehub.domain.shared.exception.InvalidEventPayloadException;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CreateShoppingSpreeEvent extends ApplicationEvent {

    private final CreateShoppingSpreeEventPayload payload;

    public CreateShoppingSpreeEvent(Object source, CreateShoppingSpreeEventPayload payload) {
        super(source);
        if (payload == null) {
            throw new InvalidEventPayloadException("CreateShoppingSpreeEventPayload cannot be null");
        }
        this.payload = payload;
    }
}
