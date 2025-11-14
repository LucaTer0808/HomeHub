package com.terfehr.homehub.application.event;

import com.terfehr.homehub.application.event.payload.DeleteHouseholdEventPayload;
import com.terfehr.homehub.domain.shared.exception.InvalidEventPayloadException;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class DeleteHouseholdEvent extends ApplicationEvent {

    private final DeleteHouseholdEventPayload payload;

    public DeleteHouseholdEvent(Object source, DeleteHouseholdEventPayload payload) {
        super(source);
        if (payload == null) {
            throw new InvalidEventPayloadException("DeleteHouseholdEventPayload cannot be null");
        }
        this.payload = payload;
    }
}
