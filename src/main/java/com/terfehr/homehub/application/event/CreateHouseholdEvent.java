package com.terfehr.homehub.application.event;

import com.terfehr.homehub.application.event.payload.CreateHouseholdEventPayload;
import com.terfehr.homehub.domain.shared.exception.InvalidEventPayloadException;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CreateHouseholdEvent extends ApplicationEvent {

    private final CreateHouseholdEventPayload payload;

    public CreateHouseholdEvent(Object source, CreateHouseholdEventPayload payload) throws InvalidEventPayloadException {
        super(source);
        if (payload == null) {
            throw new InvalidEventPayloadException("CreateHouseholdEventPayload cannot be null");
        }
        this.payload = payload;
    }
}
