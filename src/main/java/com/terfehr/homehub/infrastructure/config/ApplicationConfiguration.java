package com.terfehr.homehub.infrastructure.config;

import com.terfehr.homehub.domain.household.repository.UserRepositoryInterface;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Configuration class for the application. This class is responsible for initializing
 * the necessary components and dependencies required for the application's operation.
 * Specifically, it manages configuration related to user data handling by utilizing
 * a UserRepositoryInterface instance.
 */
@Configuration
public class ApplicationConfiguration {
    private final UserRepositoryInterface userRepository;

    /**
     * Constructs an instance of the ApplicationConfiguration class which initializes the
     * configuration with a UserRepositoryInterface to manage user-related database operations.
     *
     * @param userRepository the UserRepositoryInterface instance responsible for performing
     *                       CRUD operations and querying user data
     */
    public ApplicationConfiguration(UserRepositoryInterface userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Creates and configures a UserDetailsService bean, which provides user-specific data
     * for the authentication process. This method retrieves user details based on the given
     * username by querying the user repository. If the username is not found, it throws a
     * UsernameNotFoundException.
     *
     * @return a UserDetailsService that loads user details by username
     *         or throws a UsernameNotFoundException if the user is not found
     */
    @Bean
    UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /**
     * Creates and provides a {@link BCryptPasswordEncoder} bean for password encoding.
     * This bean is used to securely hash and verify passwords in the application.
     *
     * @return an instance of {@link BCryptPasswordEncoder} for password encryption
     */
    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Creates and provides an {@link AuthenticationManager} bean by utilizing the given
     * {@link AuthenticationConfiguration}. This method retrieves and returns the authentication
     * manager from the provided configuration, enabling authentication processes in the application.
     *
     * @param authenticationConfiguration the {@link AuthenticationConfiguration} instance that provides
     *                                     the authentication manager
     * @return the {@link AuthenticationManager} retrieved from the given configuration
     * @throws Exception if an error occurs during the retrieval of the authentication manager
     */
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Configures and provides an {@link AuthenticationProvider} bean for the application.
     * This method initializes a {@link DaoAuthenticationProvider} that works with a
     * {@link UserDetailsService} to retrieve user-specific data and a {@link BCryptPasswordEncoder}
     * for secure password processing. The configured authentication provider is used in
     * the authentication process to validate user credentials.
     *
     * @return an {@link AuthenticationProvider} configured with a user details service and password encoder
     */
    @Bean
    AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

}
