package com.terfehr.homehub.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class VerifyUserRequest implements RequestInterface {

    private String verificationCode;

    /**
     * Normalizes and validates the verification code by calling the normalize and validateVerificationCode methods.
     *
     * @return True, if the verification code is valid. False otherwise.
     */
    public boolean validate() {
        normalize();
        return validateVerificationCode();
    }

    /**
     * Normalizes the verification code by trimming the value. If it is null, it won't be changed at all.
     */
    private void normalize() {
        verificationCode = verificationCode != null ? verificationCode.trim() : null;
    }

    /**
     * Validates the verification code by ensuring it is not null.
     *
     * @return True, if the verification code is not null. False otherwise.
     */
    private boolean validateVerificationCode() {
        return verificationCode != null;
    }
}
