package com.makingscience.levelupproject.model;

import com.makingscience.levelupproject.model.entities.postgre.AcquiringTransaction;
import com.makingscience.levelupproject.model.enums.AcquiringTransactionStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@Getter
public class AcquiringTransactionDTO {


    private Long id;

    private AcquiringTransactionStatus acquiringTransactionStatus;

    private Double amount;

    private Double refundAmount;

    private Long reservationId;

    public static AcquiringTransactionDTO of(AcquiringTransaction transaction) {
        AcquiringTransactionDTO dto = new AcquiringTransactionDTO();
        dto.setId(transaction.getId());
        dto.setAmount(transaction.getAmount());
        dto.setAcquiringTransactionStatus(transaction.getAcquiringTransactionStatus());
        dto.setRefundAmount(transaction.getRefundAmount());
        dto.setReservationId(transaction.getReservation().getId());
        return dto;
    }


}
