package com.terfehr.homehub.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Locale;

/**
 * Request for logging in a User with his email.
 */
@NoArgsConstructor
@Getter
public class UserLoginRequest implements RequestInterface{

    private String email;
    private String password;

    private void normalize() {
        password = password != null ?  password.trim() : null;
        email = email != null ? email.trim().toLowerCase(Locale.ROOT) : null;
    }

    /**
     * Normalizes and validates the given parameters by orchestrating to the internal validation methods.
     *
     * @return True, if all parameters are valid. False otherwise.
     */
    public boolean validate() {
        normalize();
        return validateEmail() && validatePassword();
    }

    /**
     * Validates the Email of the Request. It cannot be null.
     *
     * @return True, if the Email is not null. False otherwise.
     */
    private boolean validateEmail() {
        return email != null;
    }

    /**
     * Validates the given Password of the Request. It cannot be null.
     *
     * @return True, if the given Password is valid. False otherwise.
     */
    private boolean validatePassword() {
        return password != null;
    }
}
