package com.terfehr.homehub.application.interfaces;

import com.terfehr.homehub.application.exception.UserNotFoundException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;

public interface AuthUserProviderInterface {

    /**
     * Returns the currently logged in user.
     */
    String getUsername() throws AuthenticationCredentialsNotFoundException, UserNotFoundException;
}
