package com.terfehr.homehub.domain.household.event;

import com.terfehr.homehub.domain.household.event.payload.RefreshVerificationCodeEventPayload;
import com.terfehr.homehub.domain.shared.exception.InvalidEventPayloadException;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class RefreshVerificationCodeEvent extends ApplicationEvent {

    private final RefreshVerificationCodeEventPayload eventPayload;

    /**
     * Constructor for a RefreshVerificationCodeEvent. If the given EventPayload is invalid, an exception is thrown.
     *
     * @param source The source of the event. Usually the calling ApplicationService.
     * @param eventPayload The payload of the event.
     * @throws InvalidEventPayloadException If the given payload is invalid.
     */
    public RefreshVerificationCodeEvent(Object source, RefreshVerificationCodeEventPayload eventPayload) throws InvalidEventPayloadException {
        super(source);
        if (eventPayload == null)
            throw new InvalidEventPayloadException("eventPayload cannot be null");
        this.eventPayload = eventPayload;
    }
}
