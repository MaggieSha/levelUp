package com.makingscience.levelupproject.facade;

import com.makingscience.levelupproject.model.dto.UserDTO;
import com.makingscience.levelupproject.model.entities.postgre.User;
import com.makingscience.levelupproject.model.params.UpdateUserParam;
import com.makingscience.levelupproject.service.ProfileService;
import com.makingscience.levelupproject.service.UserService;
import com.makingscience.levelupproject.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProfileFacade {

    private final UserService userService;
    private final JwtUtils jwtUtils;

    public UserDTO updateUser(UpdateUserParam param) {
        User authenticatedUser = jwtUtils.getAuthenticatedUser();
        if(param.getFirstName()!=null)authenticatedUser.setFirstName(param.getFirstName());
        if(param.getLastName()!=null)authenticatedUser.setLastName(param.getFirstName());

        authenticatedUser = userService.save(authenticatedUser);
        return UserDTO.of(authenticatedUser);


    }
}
