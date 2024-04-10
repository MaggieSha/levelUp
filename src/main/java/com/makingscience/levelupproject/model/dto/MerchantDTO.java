package com.makingscience.levelupproject.model.dto;

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
    private String categoryName;

    private String name;

    private String phone;

    private String email;

    private String iban;

    private String identificationNumber;

    private String image;

    private String documentAddress;

    private MerchantStatus status;
    private Double rating;

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
        merchantDTO.setCategoryName(merchant.getCategory().getName());
        return merchantDTO;
    }

    public MerchantDTO(UUID id, String name, Long categoryId,String categoryName,String email,String mobileNumber,MerchantStatus status,Double rating) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.email = email;
        this.phone = mobileNumber;
        this.status = status;

    }

    public static MerchantDTO toShortDTO(Merchant merchant) {
        MerchantDTO merchantDTO = new MerchantDTO();
        merchantDTO.setId(merchant.getId());
        merchantDTO.setCategoryId(merchant.getCategory().getId());
        merchantDTO.setName(merchant.getName());
        merchantDTO.setEmail(merchant.getEmail());
        merchantDTO.setStatus(merchant.getStatus());
        merchantDTO.setPhone(merchant.getPhone());
        merchantDTO.setCategoryName(merchant.getCategory().getName());
        return merchantDTO;
    }
}
