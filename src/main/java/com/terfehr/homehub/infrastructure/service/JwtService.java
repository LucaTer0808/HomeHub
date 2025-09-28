package com.terfehr.homehub.infrastructure.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Service class responsible for generating, validating, and extracting information
 * from JSON Web Tokens (JWTs). This service handles tasks such as creating
 * signed JWTs, verifying token authenticity, and extracting claims such as
 * username and expiration time.
 *
 * The class relies on configured properties for the secret key and token expiration
 * time, making it adaptable to different environments via configuration files.
 *
 * Annotations:
 * - {@code @Service}: Indicates that this class is a Spring service component,
 * suitable for dependency injection.
 *
 * Dependencies:
 * - Spring Framework for service annotations and configuration property injection.
 * - JJWT library for working with JSON Web Tokens.
 */
@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpirationTime;

    /**
     * Extracts the username from the provided JSON Web Token (JWT).
     *
     * @param token the JSON Web Token (JWT) from which the username is to be extracted
     * @return the username extracted from the token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a specific claim from the JWT by applying the provided claims resolver function.
     *
     * @param token the JSON Web Token (JWT) from which the claim is to be extracted
     * @param claimsResolver a function that defines how the claim is resolved from the Claims object
     * @param <T> the type of the claim being extracted
     * @return the resolved claim of type T
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Generates a JSON Web Token (JWT) for the specified user without additional claims.
     *
     * @param userDetails the user details containing authentication information, primarily the username
     * @return a signed JWT as a String
     */
    public String generateToken (UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Generates a JSON Web Token (JWT) for the specified user with additional claims.
     *
     * @param extractClaims a map containing custom claims to be included in the token
     * @param userDetails the user details containing authentication information, primarily the username
     * @return a signed JWT as a String
     */
    public String generateToken (Map<String, Object> extractClaims, UserDetails userDetails) {
        return buildToken(extractClaims, userDetails, jwtExpirationTime);
    }

    /**
     * Builds a JSON Web Token (JWT) using the provided claims, user details, and expiration time.
     *
     * @param claims a map containing custom claims to include in the token
     * @param userDetails the user details containing authentication information, primarily the username
     * @param expirationTime the time (in milliseconds) after which the token will expire
     * @return a signed JWT as a String
     */
    private String buildToken(Map<String, Object> claims, UserDetails userDetails, long expirationTime) {
        return Jwts.builder().claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSignInKey())
                .compact();
    }

    /**
     * Validates the provided JSON Web Token (JWT) by checking its username and expiration status.
     *
     * @param token the JSON Web Token (JWT) to be validated
     * @param userDetails the UserDetails containing the expected username for validation
     * @return true if the token is valid, false otherwise
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Checks whether the provided JSON Web Token (JWT) has expired.
     *
     * @param token the JSON Web Token (JWT) to be checked for expiration
     * @return true if the token has expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration date from the provided JSON Web Token (JWT).
     *
     * @param token the JSON Web Token (JWT) from which the expiration date is to be extracted
     * @return the expiration date as a Date object
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts all claims from the provided JSON Web Token (JWT).
     *
     * @param token the JSON Web Token (JWT) from which the claims are to be extracted
     * @return the claims extracted from the token as a Claims object
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Retrieves the signing key used for generating and verifying JWTs.
     * The key is decoded from a base64-encoded secret key and initialized
     * for HMAC-SHA signature algorithms.
     *
     * @return the signing key as a Key object
     */
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
