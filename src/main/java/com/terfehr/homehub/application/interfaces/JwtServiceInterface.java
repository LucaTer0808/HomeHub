package com.terfehr.homehub.application.interfaces;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

public interface JwtServiceInterface {

    /**
     * Generates a JSON Web Token (JWT) for the specified user without additional claims.
     */
    String generateToken(UserDetails userDetails);

    /**
     * Returns the creation date of the specified JWT token.
     */
    Date getIssuedAt(String token);

    /**
     * Returns the date when the specified JWT token expires.
     */
    Date getExpiresAt(String token);
}
