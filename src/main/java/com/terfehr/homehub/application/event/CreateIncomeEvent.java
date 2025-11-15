package com.terfehr.homehub.application.event;

import com.terfehr.homehub.application.event.payload.CreateExpenseEventPayload;
import com.terfehr.homehub.application.event.payload.CreateIncomeEventPayload;
import com.terfehr.homehub.domain.shared.exception.InvalidEventPayloadException;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CreateIncomeEvent extends ApplicationEvent {

    private final CreateIncomeEventPayload payload;

    public CreateIncomeEvent(Object source, CreateIncomeEventPayload payload) throws InvalidEventPayloadException {
        super(source);
        if (payload == null) {
            throw new InvalidEventPayloadException("CreateExpenseEventPayload cannot be null");
        }
        this.payload = payload;
    }
}
