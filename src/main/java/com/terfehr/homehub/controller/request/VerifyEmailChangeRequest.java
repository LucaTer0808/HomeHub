package com.terfehr.homehub.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class VerifyEmailChangeRequest implements RequestInterface {

    private String emailChangeCode;

    /**
     * Validates the request after normalization
     *
     * @return true, if the request is valid. false otherwise
     */
    public boolean validate() {
        normalize();
        return emailChangeCode != null;
    }

    /**
     * Normalizes the verification code by trimming the value. If it is null, it won't be changed at all.
     */
    private void normalize() {
        emailChangeCode = emailChangeCode != null ? emailChangeCode.trim() : null;
    }


}
