package com.makingscience.levelupproject.model.entities.postgre;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Getter
@Setter
@Table(name = "payment_transaction")
@RequiredArgsConstructor
public class PaymentTransaction {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", updatable = false)
    protected LocalDateTime createdAt = LocalDateTime.now(ZoneId.of("Asia/Tbilisi"));

    @Column(name = "amount")
    private Double amount;

    @Column(name = "commission")
    private Double commission;

    @OneToOne
    @JoinColumn(unique = true, name = "reservation_id", referencedColumnName = "id")
    private Reservation reservation;
}
