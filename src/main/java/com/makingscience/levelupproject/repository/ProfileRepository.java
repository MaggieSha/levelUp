package com.makingscience.levelupproject.repository;


import com.makingscience.levelupproject.model.entities.postgre.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProfileRepository extends JpaRepository<User, UUID> {
}
