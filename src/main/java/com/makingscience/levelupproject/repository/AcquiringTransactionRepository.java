package com.makingscience.levelupproject.repository;

import com.makingscience.levelupproject.model.entities.postgre.AcquiringTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AcquiringTransactionRepository extends JpaRepository<AcquiringTransaction,Long> {

   @Query("select t from AcquiringTransaction  t where t.reservation.id = :reservationId")
    AcquiringTransaction getByReservationId(Long reservationId);
}
