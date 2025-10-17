package com.terfehr.homehub.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Locale;

@Getter
@NoArgsConstructor
public class ChangeEmailRequest implements RequestInterface {

    private String email;

    public boolean validate() {
        normalize();
        return validateEmail(email);
    }

    private void normalize() {
        email = email != null ? email.trim().toLowerCase(Locale.ROOT) : null;
    }

    private boolean validateEmail(String email) {
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email != null && email.matches(regex);
    }
}
