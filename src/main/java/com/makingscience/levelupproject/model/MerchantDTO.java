package com.makingscience.levelupproject.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.makingscience.levelupproject.model.entities.postgre.Merchant;
import com.makingscience.levelupproject.model.enums.MerchantStatus;
import lombok.*;

import java.util.UUID;

@Setter
@NoArgsConstructor
@Getter
@ToString
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MerchantDTO {
    private UUID id;


    private Long categoryId;


    private String name;

    private String phone;

    private String email;

    private String iban;

    private String identificationNumber;

    private String image;

    private String documentAddress;

    private MerchantStatus status;

    public static MerchantDTO of(Merchant merchant) {
        MerchantDTO merchantDTO = new MerchantDTO();
        merchantDTO.setId(merchant.getId());
        merchantDTO.setCategoryId(merchant.getCategory().getId());
        merchantDTO.setName(merchant.getName());
        merchantDTO.setEmail(merchant.getEmail());
        merchantDTO.setIban(merchant.getIban());
        merchantDTO.setIdentificationNumber(merchant.getIdentificationNumber());
        merchantDTO.setImage(merchant.getImage());
        merchantDTO.setDocumentAddress(merchant.getDocumentAddress());
        merchantDTO.setStatus(merchant.getStatus());
        merchantDTO.setPhone(merchant.getPhone());
        return merchantDTO;
    }
}
