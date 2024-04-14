package com.makingscience.levelupproject.controller;

import com.makingscience.levelupproject.facade.ProfileFacade;
import com.makingscience.levelupproject.model.dto.UserDTO;
import com.makingscience.levelupproject.model.params.UpdateUserParam;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ProfileController {
    private final ProfileFacade profileFacade;

    @PutMapping
    public ResponseEntity<UserDTO> sendSMS(@Valid @RequestBody UpdateUserParam param) {
        UserDTO userDTO = profileFacade.updateUser(param);
        return ResponseEntity.ok(userDTO);
    }

}
