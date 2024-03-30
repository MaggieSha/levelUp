package com.makingscience.levelupproject.model.params;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserRegistrationParam {

    private String email;
    private String code;
    private String firstName;
    private String lastName;
    private String contactPhone;

}
