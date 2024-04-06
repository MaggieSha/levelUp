package com.makingscience.levelupproject.service;


import com.makingscience.levelupproject.model.entities.postgre.User;
import com.makingscience.levelupproject.model.enums.UserStatus;
import com.makingscience.levelupproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> getByEmail(String email) {
        return userRepository.getByEmailAndStatus(email, UserStatus.ACTIVE);
    }

    public User save(User userAccount) {
        return userRepository.save(userAccount);
    }

    public Optional<User> getByContactPhone(String contactPhone) {
        return userRepository.getByContactPhoneAndStatus(contactPhone,UserStatus.ACTIVE);
    }
}
