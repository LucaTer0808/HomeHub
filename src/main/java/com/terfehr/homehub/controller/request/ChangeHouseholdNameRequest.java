package com.terfehr.homehub.controller.request;

import lombok.Getter;

@Getter
public class ChangeHouseholdNameRequest implements RequestInterface {

    private String name;

    public boolean validate() {
        normalize();
        return name != null && !name.isEmpty();
    }

    private void normalize() {
        name = name != null ? name.trim() : null;
    }
}
