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
public class UpdateBranchParam {
    @NotNull(message = "id-Is required")
    private UUID id;

    @Pattern(regexp = "^GE\\d{2}[A-Z]{2}\\d{16}$", message = "iban-Invalid Format")
    private String iban;

    private String address;

    @Pattern(regexp = "^(?:\\d{9}|\\d{12})$", message = "Invalid format of mobileNumber")
    private String contactPhone;

    private String imageAddress;

    private String name;

    private Double reserveFee;
}
