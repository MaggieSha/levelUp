package com.makingscience.levelupproject.repository;

import com.makingscience.levelupproject.model.entities.postgre.Reservation;
import com.makingscience.levelupproject.model.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository  extends JpaRepository<Reservation,Long> {

    @Query("select r from Reservation r where r.reservationStatus in :statuses and r.reservationDay = :reservationDay")
    List<Reservation> getByStatusAndReservationDay(List<ReservationStatus> statuses, LocalDate reservationDay);


    @Query("select r from Reservation  r where r.id = :reservationId and r.user.id = :id")
    Optional<Reservation> findByIdAndUser(Long reservationId, UUID id);
}
