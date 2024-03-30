package com.makingscience.levelupproject.repository;

import com.makingscience.levelupproject.model.entities.redis.UserOtp;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserOtpRepository  extends CrudRepository<UserOtp, String> {
    Optional<UserOtp> findByEmail(String email);
}
