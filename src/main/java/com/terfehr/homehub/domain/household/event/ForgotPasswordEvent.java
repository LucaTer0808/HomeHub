package com.terfehr.homehub.domain.household.event;

import com.terfehr.homehub.domain.household.event.payload.ForgotPasswordEventPayload;
import com.terfehr.homehub.domain.shared.exception.InvalidEventPayloadException;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ForgotPasswordEvent extends ApplicationEvent {

    private final ForgotPasswordEventPayload eventPayload;

    /**
     * Constructor for a ForgotPasswordEvent. If the given EventPayload is invalid, an exception is thrown.
     *
     * @param source The source of the event. Usually the calling ApplicationService.
     * @param eventPayload The payload of the event.
     * @throws InvalidEventPayloadException If the given payload is invalid.
     */
    public ForgotPasswordEvent(Object source, ForgotPasswordEventPayload eventPayload) throws InvalidEventPayloadException {
        super(source);
        if (eventPayload == null)
            throw new InvalidEventPayloadException("eventPayload cannot be null");
        this.eventPayload = eventPayload;
    }
}
