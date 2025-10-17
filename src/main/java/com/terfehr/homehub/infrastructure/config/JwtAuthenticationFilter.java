package com.terfehr.homehub.infrastructure.config;

import com.terfehr.homehub.infrastructure.service.JwtService;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

/**
 * <p>Filter responsible for authenticating incoming HTTP requests based on
 * JSON Web Tokens (JWT). The filter intercepts requests, validates the
 * provided JWT, and configures the security context if the token is valid.
 * If any exception occurs during the authentication process, it is handled
 * by the configured exception resolver.</p>
 *
 * <p>This filter extends {@link OncePerRequestFilter} to ensure it runs once per
 * request within a single request thread.</p>
 *
 * <p>Dependencies:
 * <ul>
 * <li>{@link HandlerExceptionResolver} for handling exceptions during authentication.</li>
 * <li>{@link JwtService} for token validation and extraction of user details.</li>
 * <li>{@link UserDetailsService} for loading user-specific data from the security </li>
 *   layer.
 * </ul>
 * </p>
 *
 * <p>Responsibilities:
 * <ul>
 * <li>Extract and validate the JWT from the "Authorization" header of the request.</li>
 * <li>If valid, retrieve user details and set the authentication in the security context.</li>
 * <li>Handle exceptions occurring during the authentication process.</li>
 * </ul>
 * </p>
 */
@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Processes an incoming HTTP request to authenticate a user based on the provided JWT
     * (JSON Web Token). The method checks the "Authorization" header for a bearer token,
     * extracts the token, validates it using the JwtService, and sets the user authentication
     * if the token is valid. If any exception occurs during the process, it is handled via
     * the registered exception resolver.
     *
     * @param request the incoming {@link HttpServletRequest} object containing client request data
     * @param response the {@link HttpServletResponse} object used to send responses back to the client
     * @param filterChain the {@link FilterChain} responsible for delegating further request processing
     * @throws ServletException if an error occurs in servlet operations
     * @throws IOException if an I/O error occurs during processing
     */
    @Override
    protected void doFilterInternal (
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            final String jwt = authHeader.substring(7);
            final String username = jwtService.extractUsername(jwt);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (username != null && authentication == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }

    /**
     * Decides whether the current {@link HttpServletRequest} should be filtered or not.
     * If its ServletPath starts with "/auth/", it should not be filtered.
     *
     * @param request The Request to check
     * @return True, if it should not be filtered. False otherwise.
     */
    @Override
    public boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().startsWith("/auth/");
    }
}
