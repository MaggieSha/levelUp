package com.makingscience.levelupproject.repository;

import com.makingscience.levelupproject.model.entities.postgre.User;
import com.makingscience.levelupproject.model.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> getByEmailAndStatus(String email, UserStatus status);

    Optional<User> getByContactPhoneAndStatus(String contactPhone,UserStatus status);
}
