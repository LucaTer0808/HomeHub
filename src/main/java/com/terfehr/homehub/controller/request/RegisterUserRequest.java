package com.terfehr.homehub.controller.request;

import jakarta.security.auth.message.callback.PrivateKeyCallback;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Locale;

@NoArgsConstructor
@Getter
public class RegisterUserRequest implements RequestInterface {

    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String confirmPassword;

    /**
     * Normalizes the given Email and Username and both passwords by converting it to lower case and trimming it of leading and following white spaces.
     * If one of the fields is null, it won't be changed at all.
     */
    private void normalize() {
        username =  username != null ? username.trim().toLowerCase(Locale.ROOT) : null;
        email =  email != null ? email.trim().toLowerCase(Locale.ROOT) : null;
        password = password != null ? password.trim() : null;
        firstName = firstName != null ? firstName.trim() : null;
        lastName = lastName != null ? lastName.trim() : null;
        confirmPassword = confirmPassword != null ? confirmPassword.trim() : null;
    }

    /**
     * Validates the given request after normalizing all its data by orchestrating to the corresponding validation method.
     *
     * @return True, if the input is valid. False otherwise.
     */
    public boolean validate() {
        normalize();
        return validateUsername(username) && validateEmail(email) && validatePasswords(password, confirmPassword) && validateNames(firstName, lastName);
    }

    /**
     * Validates the given username. It has to be not null and at least have a length of 5.
     *
     * @param username The username to validate.
     * @return True, if the username is valid. False otherwise
     */
    private boolean validateUsername(String username) {
        return username != null && username.length() >= 5;
    }

    /**
     * Validates the given Email. It has to fit commonly used email standards.
     *
     * @param email The email to validate.
     * @return True, if the email is valid. False otherwise.
     */
    private boolean validateEmail(String email) {
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email != null && email.matches(regex);
    }

    /**
     * Validates the given password(s). They have to match each other, both be not null, be at least 8 digits long and contain
     * at least 1 upper case and 1 lower case letter, one "special icon" and one number.
     *
     * @param password The first password to verify.
     * @param confirmPassword The second password that has to match the first one.
     * @return True, if the password is valid. False otherwise.
     */
    private boolean validatePasswords(String password, String confirmPassword) {
        return password != null &&
                password.equals(confirmPassword);
    }

    /**
     * Validates the given names. They have to be non-null and non-blank.
     *
     * @param firstName The first name to validate.
     * @param lastName The last name to validate.
     * @return True, if the names are valid. False otherwise.
     */
    private boolean validateNames(String firstName, String lastName) {
        return firstName != null && !firstName.isBlank() && lastName != null && !lastName.isBlank();
    }
}
