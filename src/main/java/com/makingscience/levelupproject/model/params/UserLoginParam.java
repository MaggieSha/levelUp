package com.makingscience.levelupproject.model.params;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserLoginParam {
    private String email;
    private String code;
}

