package com.makingscience.levelupproject.service;

import com.makingscience.levelupproject.model.entities.redis.UserOtp;
import com.makingscience.levelupproject.repository.UserOtpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserOtpService {
    private final UserOtpRepository userOtpRepository;

    public Optional<UserOtp> getByEmail(String email) {
        return userOtpRepository.findByEmail(email);
    }

    public void delete(UserOtp u) {
        userOtpRepository.delete(u);
    }

    public UserOtp save(UserOtp userOtp) {
        return userOtpRepository.save(userOtp);
    }
}
