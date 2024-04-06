package com.makingscience.levelupproject.model.params;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalTime;
import java.util.UUID;

@Setter
@NoArgsConstructor
@Getter
@ToString
public class CreateBranchParam {
    @NotNull(message = "merchantId-Is required")
    private UUID merchantId;

    @Pattern(regexp = "^GE\\d{2}[A-Z]{2}\\d{16}$", message = "iban-Invalid Format")
    private String iban;

    @NotBlank(message = "address-Is required")
    private String address;

    @NotBlank(message = "contactPhone-Is required")
    @Pattern(regexp = "^(?:\\d{9}|\\d{12})$", message = "Invalid format of mobileNumber")
    private String contactPhone;

    private String imageAddress;

    @NotBlank(message = "name-Is required")
    private String name;

    @NotNull(message = "reserveFee-Is required")
    private Double reserveFee;









}
