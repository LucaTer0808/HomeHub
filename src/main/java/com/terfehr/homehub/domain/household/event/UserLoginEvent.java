package com.terfehr.homehub.domain.household.event;

import com.terfehr.homehub.domain.household.event.payload.UserLoginEventPayload;
import com.terfehr.homehub.domain.shared.exception.InvalidEventPayloadException;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

/**
 * Event that gets published when a user logs in. It contains the user that just logged in.
 */
@Getter
public class UserLoginEvent extends ApplicationEvent {

    private final UserLoginEventPayload payload;
    /**
     * Constructor for UserLoginEvent.
     *
     * @param source The object publishing the event.
     * @param payload The payload of the event, in this case a UserLoginEventPayload.
     */
    public UserLoginEvent(Object source, UserLoginEventPayload payload) {
        super(source);
        if (!validatePayload(payload)) {
            throw new InvalidEventPayloadException("The given UserLoginEventPayload is invalid. It most likely is null");
        }
        this.payload = payload;
    }

    /**
     * Validates the given payload.
     *
     * @param payload The payload to validate. It cannot be null.
     * @return True, if the payload is not null. False otherwise.
     */
    private boolean validatePayload(UserLoginEventPayload payload) {
        return payload != null;
    }
}
