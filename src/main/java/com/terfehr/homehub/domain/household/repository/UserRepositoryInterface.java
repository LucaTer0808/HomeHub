package com.terfehr.homehub.domain.household.repository;

import com.terfehr.homehub.domain.household.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepositoryInterface extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByVerificationCode(String verificationCode);
}
