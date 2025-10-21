package com.terfehr.homehub.controller.request;

import lombok.Getter;

@Getter
public class ChangePasswordRequest implements RequestInterface {

    private String password;
    private String confirmPassword;

    public boolean validate() {
        normalize();
        return password != null && password.equals(confirmPassword);
    }

    private void normalize() {
        password = password != null ? password.trim() : null;
        confirmPassword = confirmPassword != null ? confirmPassword.trim() : null;
    }
}
