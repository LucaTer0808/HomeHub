package com.terfehr.homehub.domain.household.event;

import org.springframework.context.ApplicationEvent;

/**
 * Event that informs its listeners about the given User being registered.
 */
public class UserRegisteredEvent extends ApplicationEvent {

    public UserRegisteredEvent(Object source) {
        super(source);
    }
}
