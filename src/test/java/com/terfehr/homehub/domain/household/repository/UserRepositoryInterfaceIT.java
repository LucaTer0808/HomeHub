package com.terfehr.homehub.domain.household.repository;

import com.terfehr.homehub.domain.household.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class UserRepositoryInterfaceIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    private UserRepositoryInterface userRepository;

    @Test
    public void testFindAll() {
        User user = new User("BryanLasme04", "bryan.lasme@s04.de", "S04oleole", "123456789", LocalDateTime.now());
        userRepository.save(user);
        Optional<User> userFromDb = userRepository.findByUsername("BryanLasme04");
        assertTrue(userFromDb.isPresent());
        assertEquals(userFromDb.get().getUsername(), user.getUsername());
    }
}