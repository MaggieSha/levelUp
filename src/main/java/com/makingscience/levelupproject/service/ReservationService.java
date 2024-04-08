package com.makingscience.levelupproject.service;

import com.makingscience.levelupproject.model.entities.postgre.Reservation;
import com.makingscience.levelupproject.model.enums.ReservationStatus;
import com.makingscience.levelupproject.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public Reservation save(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getByStatusAndReservationDay(List<ReservationStatus> statuses, LocalDate reservationDay) {
        return reservationRepository.getByStatusAndReservationDay(statuses,reservationDay);
    }


    public Reservation getById(Long id) {
        return reservationRepository.findById(id).orElseThrow(()
        -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Reservation with id " + id + " not found!"));
    }

    public Reservation getByIdAndUser(Long reservationId, UUID id) {
        return reservationRepository.findByIdAndUser(reservationId,id).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Reservation with id " + id + " not found!"));
    }
}
