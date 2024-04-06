package com.makingscience.levelupproject.model.params;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Setter
@NoArgsConstructor
@Getter
@ToString
public class UpdateMerchantParam {

    @NotNull(message = "merchantId-Is required")
    private UUID id;

    private String email;

    @Pattern(regexp = "^GE\\d{2}[A-Z]{2}\\d{16}$", message = "iban-Invalid Format")
    private String iban;

    @Pattern(regexp = "^\\d{9}$", message = "Invalid format of personalNumber")
    private String identificationNumber;


    private String imageAddress;

    private String documentAddress;

    private String name;

    @Pattern(regexp = "^(?:\\d{9}|\\d{12})$", message = "Invalid format of mobileNumber")
    private String contactPhone;



}
