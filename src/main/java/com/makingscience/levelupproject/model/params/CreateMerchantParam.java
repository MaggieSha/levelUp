package com.makingscience.levelupproject.model.params;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@NoArgsConstructor
@Getter
@ToString
public class CreateMerchantParam {
    private String email;

    @NotBlank(message = "iban-Is required")
    @Pattern(regexp = "^GE\\d{2}[A-Z]{2}\\d{16}$", message = "iban-Invalid Format")
    private String iban;

    @NotBlank(message = "identificationNumber-Is required")
    @Pattern(regexp = "^\\d{9}$", message = "Invalid format of personalNumber")
    private String identificationNumber;


    private String imageAddress;

    private String documentAddress;

    @NotBlank(message = "name-Is required")
    private String name;

    @NotBlank(message = "contactPhone-Is required")
    @Pattern(regexp = "^(?:\\d{9}|\\d{12})$", message = "Invalid format of mobileNumber")
    private String contactPhone;

    @NotNull(message = "categoryId-Is required")
    private Long categoryId;
}
