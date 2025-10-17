package com.terfehr.homehub.infrastructure.service;

import com.terfehr.homehub.application.exception.UserNotFoundException;
import com.terfehr.homehub.application.interfaces.AuthUserProviderInterface;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthUserProvider implements AuthUserProviderInterface {

    /**
     * {@inheritDoc}
     *
     * Extracts the Authentication object from the security context and returns the contained principal,
     * which in this case is the desired UserDetails implementing object.
     *
     * @return The username of the authenticated user as String.
     * @throws AuthenticationCredentialsNotFoundException If the given Authentication object is null.
     * @throws UserNotFoundException If the given Authentication object does not contain a UserDetails object.
     */
    public String getUsername() throws AuthenticationCredentialsNotFoundException, UserNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new AuthenticationCredentialsNotFoundException("User not found");
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof UserDetails userDetails)) {
            throw new UserNotFoundException("User details not found");
        }

        return userDetails.getUsername();
    }
}
