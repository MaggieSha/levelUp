package com.makingscience.levelupproject.model.entities.postgre;

import com.makingscience.levelupproject.model.enums.AcquiringTransactionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Getter
@Setter
@Table(name = "acquiring_transaction")
@RequiredArgsConstructor
public class AcquiringTransaction {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", updatable = false)
    protected LocalDateTime createdAt = LocalDateTime.now(ZoneId.of("Asia/Tbilisi"));

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AcquiringTransactionStatus acquiringTransactionStatus;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "refund_amount")
    private Double refundAmount;

    @Column(name = "refund_date")
    private LocalDateTime refundDate;

    @OneToOne
    @JoinColumn(unique = true, name = "reservation_id", referencedColumnName = "id")
    private Reservation reservation;


}
