package com.makingscience.levelupproject.service;

import com.makingscience.levelupproject.model.entities.redis.UserOtp;
import com.makingscience.levelupproject.model.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
    private final UserOtpService userOtpService;
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        userService.getByEmail(username)
                .orElseThrow(() -> new NotFoundException("User with email " + username + " not found!"));


        UserOtp userOtp = userOtpService.getByEmail(username).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.PRECONDITION_REQUIRED, String.format("OTP not send on email %s ", username))
        );

        return new User(username, userOtp.getCode(), List.of(new SimpleGrantedAuthority(Role.USER.name())));
    }
}
