package com.terfehr.homehub.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ResetPasswordRequest implements RequestInterface {

    private String password;
    private String confirmPassword;
    private String forgotPasswordCode;

    public boolean validate() {
        normalize();
        return validatePasswords() && validateForgotPasswordCode();
    }

    private void normalize() {
        password = password != null ? password.trim() : null;
        confirmPassword = confirmPassword != null ? confirmPassword.trim() : null;
        forgotPasswordCode = forgotPasswordCode != null ? forgotPasswordCode.trim() : null;
    }

    private boolean validatePasswords() {
        return password != null && password.equals(confirmPassword);
    }

    private boolean validateForgotPasswordCode() {
        return forgotPasswordCode != null;
    }
}
