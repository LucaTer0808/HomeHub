package com.terfehr.homehub.domain.household.event;

import com.terfehr.homehub.domain.household.event.payload.UserVerifiedEventPayload;
import com.terfehr.homehub.domain.shared.exception.InvalidEventPayloadException;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Event that gets published when a user verifies their account.
 */
@Getter
public class UserVerifiedEvent extends ApplicationEvent {

    private final UserVerifiedEventPayload payload;
    /**
     * Constructor for UserVerifiedEvent.
     *
     * @param source The object publishing the event.
     * @param payload The payload of the event, in this case a UserVerifiedEventPayload.
     * @throws InvalidEventPayloadException If the given payload is invalid.
     */
    public UserVerifiedEvent(Object source, UserVerifiedEventPayload payload) throws InvalidEventPayloadException {
        super(source);
        if (!validatePayload(payload)) {
            throw new InvalidEventPayloadException("The given UserVerifiedEventPayload is invalid. It most likely is null");
        }
        this.payload = payload;
    }

    /**
     * Validates the given payload.
     *
     * @param payload The payload to validate. It cannot be null.
     * @return True, if the payload is not null. False otherwise.
     */
    private boolean validatePayload(UserVerifiedEventPayload payload) {
        return payload != null;
    }
}
