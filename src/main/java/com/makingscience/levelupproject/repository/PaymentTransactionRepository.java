package com.makingscience.levelupproject.repository;

import com.makingscience.levelupproject.model.entities.postgre.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction,Long> {
    @Query("select p from PaymentTransaction  p where p.reservation.id = :id")
    Optional<PaymentTransaction> getByReservationId(Long id);
}
