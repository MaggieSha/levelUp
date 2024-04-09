package com.makingscience.levelupproject.service;

import com.makingscience.levelupproject.model.entities.postgre.Reservation;
import com.makingscience.levelupproject.model.entities.postgre.User;
import com.makingscience.levelupproject.model.enums.ReservationStatus;
import com.makingscience.levelupproject.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
                -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Reservation with id " + reservationId + " not found!"));
    }

    public Page<Reservation> getByUser(UUID userId, Pageable pageable) {
        return reservationRepository.findAllByUser(userId,pageable);
    }

    public Page<Reservation> getByMerchantId(UUID merchantId, Pageable pageable) {
        return reservationRepository.findAllByMerchant(merchantId,pageable);
    }

    public Page<Reservation> getByBranchId(UUID branchId, Pageable pageable) {
        return reservationRepository.findAllByBranch(branchId,pageable);

    }
}
