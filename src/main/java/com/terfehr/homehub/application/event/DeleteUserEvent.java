package com.terfehr.homehub.application.event;

import com.terfehr.homehub.application.event.payload.DeleteUserEventPayload;
import com.terfehr.homehub.domain.shared.exception.InvalidEventPayloadException;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class DeleteUserEvent extends ApplicationEvent {

    private final DeleteUserEventPayload payload;

    public DeleteUserEvent(Object source, DeleteUserEventPayload payload) throws InvalidEventPayloadException {
        super(source);
        if (payload == null) {
            throw new InvalidEventPayloadException("DeleteUserEventPayload cannot be null");
        }
        this.payload = payload;
    }
}
