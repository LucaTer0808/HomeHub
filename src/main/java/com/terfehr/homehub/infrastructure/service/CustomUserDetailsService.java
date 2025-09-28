package com.terfehr.homehub.infrastructure.service;

import com.terfehr.homehub.domain.household.repository.UserRepositoryInterface;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepositoryInterface userRepository;

    /**
     * Loads the user details for the given username.
     * This method fetches user information from the persistence layer
     * based on the provided username. If the user is not found,
     * a {@code UsernameNotFoundException} is thrown.
     *
     * @param username the username of the user to be loaded
     * @return the {@link UserDetails} of the user associated with the given username
     * @throws UsernameNotFoundException if no user is found for the given username
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}