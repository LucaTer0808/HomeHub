package com.terfehr.homehub.domain.household.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@NoArgsConstructor
@Getter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private boolean enabled;
    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Roommate> roommates;

    /**
     * Constructs a new User instance with the specified username, email, and hashed password.
     * Initializes an empty set of roommates for the user. Note that the requirements for username, email, and password
     * are checked not in here because it is no business logic, but rather a security concern that should be handled elsewhere.
     *
     * @param username the username of the user
     * @param email the email address of the user
     * @param hashedPassword the hashed password for the user's authentication
     */
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.enabled = false;
        roommates = new HashSet<>();
    }

    /**
     * Adds a roommate to the current user's list of roommates after validating the input.
     * This method ensures that the roommate is valid and properly associated with the user.
     *
     * @param roommate the Roommate object to be added to the user's list of roommates
     *                 if it passes validation
     * @throws IllegalArgumentException if the provided roommate is invalid
     */
    public void addRoommate(Roommate roommate) {
        if (!validateRoommate(roommate)) {
            throw new IllegalArgumentException("Invalid Roommate for this User");
        }
        this.roommates.add(roommate);
    }

    /**
     * Retrieves the collection of granted authorities for the user.
     *
     * @return a collection of granted authorities associated with the user
     */
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    /**
     * Validates whether the specified roommate is associated with the current user.
     * The validation ensures that the roommate is not null and that the user linked
     * to the roommate matches the current user instance.
     *
     * @param roommate the Roommate object to validate
     * @return true if the roommate is valid and associated with the current user, false otherwise
     */
    private boolean validateRoommate(Roommate roommate) {
        return roommate != null && roommate.getUser().equals(this);
    }
}
