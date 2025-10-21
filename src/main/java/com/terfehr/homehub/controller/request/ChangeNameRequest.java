package com.terfehr.homehub.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ChangeNameRequest implements RequestInterface {

    private String firstName;
    private String lastName;

    public boolean validate() {
        normalize();
        return firstName != null && lastName != null;
    }

    private void normalize() {
        firstName = firstName != null ? firstName.trim() : null;
        lastName = lastName != null ? lastName.trim() : null;
    }
}
