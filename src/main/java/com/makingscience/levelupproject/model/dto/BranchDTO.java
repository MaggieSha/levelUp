package com.makingscience.levelupproject.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.makingscience.levelupproject.model.entities.postgre.Branch;
import com.makingscience.levelupproject.model.enums.BranchStatus;
import lombok.*;

import java.util.UUID;

@Setter
@NoArgsConstructor
@Getter
@ToString
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BranchDTO {
    private UUID id;

    private UUID merchantId;


    private String contactPhone;

    private Double rating;


    private String name;


    private Double reserveFee;

    private String iban;

    private String address;

    private String image;

    private BranchStatus status;

    public static BranchDTO of(Branch save) {
        BranchDTO branchDTO = new BranchDTO();
        branchDTO.setId(save.getId());
        branchDTO.setMerchantId(save.getMerchant().getId());
        branchDTO.setContactPhone(save.getContactPhone());
        branchDTO.setName(save.getName());
        branchDTO.setIban(save.getIban());
        branchDTO.setImage(save.getImage());
        branchDTO.setAddress(save.getAddress());
        branchDTO.setStatus(save.getStatus());
        branchDTO.setReserveFee(save.getReserveFee());

        return branchDTO;

    }
}
