package com.terfehr.homehub.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ForgotPasswordRequest {

    private String email;

    public void normalize() {
        email = email != null ? email.trim().toLowerCase() : null;
    }

    public boolean validate() {
        normalize();
        return email != null;
    }
}
