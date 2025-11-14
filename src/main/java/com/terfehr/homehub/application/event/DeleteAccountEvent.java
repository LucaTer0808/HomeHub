package com.terfehr.homehub.application.event;

import com.terfehr.homehub.application.event.payload.DeleteAccountEventPayload;
import com.terfehr.homehub.domain.shared.exception.InvalidEventPayloadException;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class DeleteAccountEvent extends ApplicationEvent {

    private final DeleteAccountEventPayload payload;

    public DeleteAccountEvent(Object source, DeleteAccountEventPayload payload) {
        super(source);
        if (payload == null) {
            throw new InvalidEventPayloadException("DeleteAccountEventPayload cannot be null");
        }
        this.payload = payload;
    }
}
