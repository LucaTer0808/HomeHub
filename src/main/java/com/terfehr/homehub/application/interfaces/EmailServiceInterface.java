package com.terfehr.homehub.application.interfaces;

import com.terfehr.homehub.domain.household.entity.User;

/**
 * Interface for everything Email-related. Enables dependency inversion.
 */
public interface EmailServiceInterface {

    /**
     * Mail sent to the user after he registered himself.
     * @param user The mail recipient.
     */
    void sendRegistrationMail(User user);

    /**
     * Mail sent to the user after he verified himself.
     *
     * @param user The mail recipient.
     */
    void sendVerificationMail(User user);

}
