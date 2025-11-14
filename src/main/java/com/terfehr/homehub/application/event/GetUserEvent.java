package com.terfehr.homehub.application.event;

import com.terfehr.homehub.application.event.payload.GetUserEventPayload;
import com.terfehr.homehub.domain.shared.exception.InvalidEventPayloadException;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class GetUserEvent extends ApplicationEvent {

    private final GetUserEventPayload payload;

    public GetUserEvent(Object source, GetUserEventPayload payload) {
        super(source);
        if (payload == null) {
            throw new InvalidEventPayloadException("GetUserEventPayload cannot be null");
        }
        this.payload = payload;
    }
}
