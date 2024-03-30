package com.makingscience.levelupproject.facade;

import com.makingscience.levelupproject.utils.JwtUtils;
import com.makingscience.levelupproject.model.enums.OtpType;
import com.makingscience.levelupproject.model.enums.Role;
import com.makingscience.levelupproject.model.enums.UserStatus;
import com.makingscience.levelupproject.model.entities.postgre.User;
import com.makingscience.levelupproject.model.entities.redis.UserOtp;
import com.makingscience.levelupproject.model.params.SendOtpParam;
import com.makingscience.levelupproject.model.params.UserLoginParam;
import com.makingscience.levelupproject.model.params.UserRegistrationParam;
import com.makingscience.levelupproject.model.token.AuthResponse;
import com.makingscience.levelupproject.service.EmailService;
import com.makingscience.levelupproject.service.UserOtpService;
import com.makingscience.levelupproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Random;


@Service
@RequiredArgsConstructor
public class AuthFacade {

    private final EmailService emailService;
    private final UserService userService;
    private final UserOtpService userOtpService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    public void sendSMS(SendOtpParam param) {
        OtpType smsType = param.getOtpType();
        String rawCode  = randomCode();
        String finalCode = rawCode;
        if (smsType.equals(OtpType.LOGIN)) {
            Optional<User> userAccount = userService.getByEmail(param.getEmail());
            if (userAccount.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account does not exist for email: " + param.getEmail());
            }

            if (userAccount.isPresent() && userAccount.get().getStatus().equals(UserStatus.BLOCKED)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Account with email: " + emailService + " is blocked!");
            }

            finalCode = passwordEncoder.encode(rawCode);
        }


        Optional<UserOtp> userSMS = userOtpService.getByEmail(param.getEmail());
        if (userSMS.isPresent()) userOtpService.delete(userSMS.get());


        UserOtp save = userOtpService.save(UserOtp.of(param,finalCode));

        System.out.println("Email service CODE = " + rawCode);

        emailService.send(param.getEmail(),rawCode);
    }


    public void register(UserRegistrationParam param) {
        UserOtp userOtp = userOtpService.getByEmail(param.getEmail()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.PRECONDITION_REQUIRED, String.format("OTP not send on email %s ", param.getEmail()))
        );
        if (userOtp.getCode().equals(param.getCode())) {
            userOtpService.delete(userOtp);
            User user = registerUser(param);
            // login and return
            return;
        }

        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Invalid OTP!");
    }

    private User registerUser(UserRegistrationParam param) {


        Optional<User> optionalUserAccount = userService.getByEmail(param.getEmail());
        if (optionalUserAccount.isPresent()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User already exists with email: " + param.getEmail());
        }
        User user = new User();
        user.setFirstName(param.getFirstName());
        user.setLastName(param.getLastName());
        user.setContactPhone(param.getContactPhone());
        user.setStatus(UserStatus.ACTIVE);
        user.setRole(Role.USER);
        user.setEmail(param.getEmail());

        return userService.save(user);


    }

    public AuthResponse login(UserLoginParam param) {

        Authentication authenticate = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                param.getEmail(), param.getCode(),List.of(new SimpleGrantedAuthority(Role.USER.name()))
                        )
                );


        return jwtUtils.generateJwtToken((org.springframework.security.core.userdetails.User) authenticate.getPrincipal());

    }
    public static String randomCode() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%04d", number);
    }
}
