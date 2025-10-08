package com.terfehr.homehub.application.service;

import com.terfehr.homehub.application.command.RegisterUserCommand;
import com.terfehr.homehub.application.dto.UserDTO;
import com.terfehr.homehub.application.exception.EmailAlreadyExistsException;
import com.terfehr.homehub.application.exception.UsernameAlreadyExistsException;
import com.terfehr.homehub.domain.household.entity.User;
import com.terfehr.homehub.domain.household.event.UserRegisteredEvent;
import com.terfehr.homehub.domain.household.exception.InvalidUserException;
import com.terfehr.homehub.domain.household.repository.UserRepositoryInterface;
import com.terfehr.homehub.domain.household.service.UserRegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegisterUserService {

    private final UserRepositoryInterface  userRepository;
    private final UserRegistrationService userRegistrationService;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher publisher;

    /**
     * Executed the command that asks for the registration of a User with the contained credentials.
     * Encodes the contained password, generates the verification code and expiration date, checks for uniqueness of username
     * and email address, persists a new User object in the Database and published an Event that informs about the creation of a new user.
     *
     * @param cmd The command containing the User credentials.
     * @return A UserDTO containing the ID, username and password of the newly created user.
     * @throws EmailAlreadyExistsException If the email address is already taken.
     * @throws UsernameAlreadyExistsException IF the username is already taken.
     * @throws InvalidUserException If the given user credentials are faulty
     */
    public UserDTO execute(RegisterUserCommand cmd) throws EmailAlreadyExistsException, UsernameAlreadyExistsException, InvalidUserException {
        String username = cmd.getUsername();
        String email =  cmd.getEmail();
        String password = passwordEncoder.encode(cmd.getPassword());
        String verificationCode = userRegistrationService.generateUniqueVerificationCode();
        LocalDateTime expiration = userRegistrationService.getVerificationCodeExpiration();

        if (!userRegistrationService.isEmailUnique(email)) {
            throw new EmailAlreadyExistsException("The given email " + email + " already exists");
        }
        if (!userRegistrationService.isUsernameUnique(username)) {
            throw new UsernameAlreadyExistsException("The given username " + username + " already exists");
        }

        User registeredUser = new User(username, email, password, verificationCode, expiration);
        userRepository.save(registeredUser);

        UserDTO registerdUserDTO = new UserDTO(registeredUser);
        publisher.publishEvent(new UserRegisteredEvent(registerdUserDTO));
        return registerdUserDTO;
    }

}
