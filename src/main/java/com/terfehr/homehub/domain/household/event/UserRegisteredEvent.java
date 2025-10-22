package com.terfehr.homehub.domain.household.event;

import com.terfehr.homehub.domain.household.event.payload.UserRegisteredEventPayload;
import com.terfehr.homehub.domain.shared.exception.InvalidDomainEventPayloadException;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Event that informs its listeners about the given User being registered.
 */
@Getter
public class UserRegisteredEvent extends ApplicationEvent {

    private final UserRegisteredEventPayload payload;

    /**
     * Constructor for UserRegisteredEvent.
     *
     * @param source The object publishing the event.
     * @param payload The payload of the event, in this case a UserRegisteredEventPayload.
     */
    public UserRegisteredEvent(Object source, UserRegisteredEventPayload payload) {
        super(source);
        if (!validatePayload(payload)) {
            throw new InvalidDomainEventPayloadException("The given UserRegisteredEventPayload is invalid. It most likely is null");
        }
        this.payload = payload;
    }

    /**
     * Validates the given payload.
     *
     * @param payload The payload to validate. It cannot be null.
     * @return True, if the payload is not null. False otherwise.
     */
    private boolean validatePayload(UserRegisteredEventPayload payload) {
        return payload != null;
    }
}
