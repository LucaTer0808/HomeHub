package com.terfehr.homehub.application.interfaces;

import com.terfehr.homehub.application.exception.UserNotFoundException;
import com.terfehr.homehub.domain.household.entity.User;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;

public interface AuthUserProviderInterface {

    /**
     * Returns the currently logged in user.
     */
    String getUsername() throws AuthenticationCredentialsNotFoundException, UserNotFoundException;

    /**
     * Returns the currently logged-in user.
     */
    User getUser() throws AuthenticationCredentialsNotFoundException, UserNotFoundException;
}
