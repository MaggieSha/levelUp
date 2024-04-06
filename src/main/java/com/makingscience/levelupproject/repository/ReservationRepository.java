package com.makingscience.levelupproject.repository;

import com.makingscience.levelupproject.model.entities.postgre.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository  extends JpaRepository<Reservation,Long> {
}
