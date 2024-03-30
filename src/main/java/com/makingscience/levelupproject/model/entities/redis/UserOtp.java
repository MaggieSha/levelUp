package com.makingscience.levelupproject.model.entities.redis;

import com.makingscience.levelupproject.model.enums.OtpType;
import com.makingscience.levelupproject.model.params.SendOtpParam;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Random;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@RedisHash(value = "otp", timeToLive = 300)
public class UserOtp {


    private String id;
    private String code;

    @Indexed
    private String email;
    private Boolean verified = false;
    private LocalDateTime sendTime = LocalDateTime.now(ZoneId.of("Asia/Tbilisi"));
    private OtpType otpType;

    public static UserOtp of(SendOtpParam param,String otp) {
        UserOtp userOtp = new UserOtp();
        userOtp.setId(UUID.randomUUID().toString());
        userOtp.setCode(otp);
        userOtp.setOtpType(param.getOtpType());
        userOtp.setEmail(param.getEmail());

        return userOtp;
    }


}
