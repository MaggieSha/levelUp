package com.makingscience.levelupproject.service;

import com.makingscience.levelupproject.model.ReservationDTO;
import com.makingscience.levelupproject.model.entities.postgre.Reservation;
import com.makingscience.levelupproject.model.entities.postgre.Slot;
import com.makingscience.levelupproject.model.entities.postgre.User;
import com.makingscience.levelupproject.model.enums.ReservationStatus;
import com.makingscience.levelupproject.model.enums.Type;
import com.makingscience.levelupproject.model.params.ReservationRequest;
import com.makingscience.levelupproject.repository.SlotRepository;
import com.makingscience.levelupproject.service.interfaces.ReservationService;
import com.makingscience.levelupproject.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReserveVisitService  implements ReservationService {

    private final JwtUtils jwtUtils;
    private final SlotRepository slotRepository;
    @Override
    public ReservationDTO reserve(ReservationRequest param) {
        User authenticatedUser = jwtUtils.getAuthenticatedUser();

        Slot slot = slotRepository.findByIdAndLock(param.getSlotId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Slot not found by id " + param.getSlotId()));
//        if(param.getReservationTime().isBefore(slot.getBranch().getReservationHours())){
//            throw new ResponseStatusException(HttpStatus.CONFLICT,"Reservation starts after " + slot.getBranch().getReservationHours());
//        }
        Set<Reservation> reservations = slot.getReservationSet();
        for(Reservation reservation : reservations) {
            if((reservation.getReservationStatus().equals(ReservationStatus.CONFIRMED) ||
                    reservation.getReservationStatus().equals(ReservationStatus.CHECKED_IN))
                    && reservation.getReservationDay().equals(param.getReservationDay())){
                throw new ResponseStatusException(HttpStatus.CONFLICT,"Slot is already reserved!");
            }
        }

        return null;
    }

    @Override
    public Type getType() {
        return Type.SALON;
    }


}
