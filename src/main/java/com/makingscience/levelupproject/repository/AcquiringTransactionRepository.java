package com.makingscience.levelupproject.repository;

import com.makingscience.levelupproject.model.entities.postgre.AcquiringTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AcquiringTransactionRepository extends JpaRepository<AcquiringTransaction,Long> {

   @Query("select t from AcquiringTransaction  t where t.reservation.id = :reservationId")
   Optional<AcquiringTransaction> getByReservationId(Long reservationId);
}
