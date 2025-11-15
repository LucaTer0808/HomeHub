package com.terfehr.homehub.application.event;

import com.terfehr.homehub.application.event.payload.DeleteTransactionEventPayload;
import com.terfehr.homehub.domain.shared.exception.InvalidEventPayloadException;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class DeleteTransactionEvent extends ApplicationEvent {

    private final DeleteTransactionEventPayload payload;

    public DeleteTransactionEvent(Object source, DeleteTransactionEventPayload payload) throws InvalidEventPayloadException {
        super(source);
        if (payload == null) {
            throw new InvalidEventPayloadException("DeleteTransactionEventPayload cannot be null");
        }
        this.payload = payload;
    }
}
