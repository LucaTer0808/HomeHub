package com.terfehr.homehub.domain.household.event;

import com.terfehr.homehub.domain.household.event.payload.TransferAdminRightsEventPayload;
import com.terfehr.homehub.domain.shared.exception.InvalidEventPayloadException;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TransferAdminRightsEvent extends ApplicationEvent {

    private final TransferAdminRightsEventPayload payload;

    public TransferAdminRightsEvent(Object source, TransferAdminRightsEventPayload payload) {
        super(source);
        if (payload == null) {
            throw new InvalidEventPayloadException("TransferAdminRightsEventPayload cannot be null");
        }
        this.payload = payload;
    }
}
