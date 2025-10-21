package com.terfehr.homehub.domain.household.event;

import com.terfehr.homehub.domain.household.event.payload.ChangeUsernameEventPayload;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ChangeUsernameEvent extends ApplicationEvent {

    private final ChangeUsernameEventPayload payload;

    public ChangeUsernameEvent(Object source, ChangeUsernameEventPayload payload) {
        super(source);
        if (payload == null) {
            throw new IllegalArgumentException("ChangeUsernameEventPayload cannot be null");
        }
        this.payload = payload;
    }
}
