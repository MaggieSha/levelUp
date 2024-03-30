package com.makingscience.levelupproject.model.params;


import com.makingscience.levelupproject.model.enums.OtpType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class SendOtpParam {
   private String email;
   private OtpType otpType;
}
