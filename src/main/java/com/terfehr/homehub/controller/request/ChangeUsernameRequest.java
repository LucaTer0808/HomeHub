package com.terfehr.homehub.controller.request;

import lombok.Getter;

import java.util.Locale;

@Getter
public class ChangeUsernameRequest implements RequestInterface {

    private String username;

    public boolean validate() {
        normalize();
        return username != null;
    }

    private void normalize() {
        username = username != null ? username.trim().toLowerCase(Locale.ROOT) : null;
    }
}
