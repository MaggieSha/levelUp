package com.makingscience.levelupproject.repository;

import com.makingscience.levelupproject.model.entities.postgre.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> getByEmail(String email);
}
