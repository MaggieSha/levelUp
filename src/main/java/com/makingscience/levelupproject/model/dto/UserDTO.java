package com.makingscience.levelupproject.model.dto;

import com.makingscience.levelupproject.model.entities.postgre.User;
import com.makingscience.levelupproject.model.enums.Role;
import com.makingscience.levelupproject.model.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

    private UUID id;

    private String email;


    private String firstName;

    private String lastName;

    private String contactPhone;


    public static UserDTO of(User authenticatedUser) {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(authenticatedUser.getEmail());
        userDTO.setFirstName(authenticatedUser.getFirstName());
        userDTO.setLastName(authenticatedUser.getLastName());
        userDTO.setContactPhone(authenticatedUser.getContactPhone());
        return userDTO;
    }
}
