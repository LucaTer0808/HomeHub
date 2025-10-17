package com.terfehr.homehub.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Locale;

@NoArgsConstructor
@Getter
public class RefreshVerificationCodeRequest implements RequestInterface {

    private String email;

    /**
     * Validates the given request after normalizing it.
     *
     * @return True, if the trimmed lower-case email is not null.
     */
    public boolean validate() {
        normalize();
        return email != null;
    }

    /**
     * Normalizes the email by trimming it off white-spaces and converting it to lower-case.
     */
    private void normalize() {
        email = email != null ? email.trim().toLowerCase(Locale.ROOT) : null;
    }
}
