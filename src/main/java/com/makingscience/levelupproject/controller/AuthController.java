package com.makingscience.levelupproject.controller;

import com.makingscience.levelupproject.facade.AuthFacade;
import com.makingscience.levelupproject.model.token.AuthResponse;
import com.makingscience.levelupproject.model.params.SendOtpParam;
import com.makingscience.levelupproject.model.params.UserLoginParam;
import com.makingscience.levelupproject.model.params.UserRegistrationParam;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AuthController {


    private final AuthFacade authFacade;

    @PostMapping("/send-sms")
    public ResponseEntity<Void> sendSMS(@Valid @RequestBody SendOtpParam param) {
        authFacade.sendSMS(param);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registration(@Valid @RequestBody UserRegistrationParam param) {
        authFacade.register(param);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody UserLoginParam param) {
        AuthResponse authResponse = authFacade.login(param);
        return  ResponseEntity.ok(authResponse);
    }
}
