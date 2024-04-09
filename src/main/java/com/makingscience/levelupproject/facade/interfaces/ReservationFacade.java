package com.makingscience.levelupproject.facade.interfaces;


import com.makingscience.levelupproject.model.ReservationDTO;
import com.makingscience.levelupproject.model.details.reservation.ReservationDetails;
import com.makingscience.levelupproject.model.entities.postgre.Reservation;
import com.makingscience.levelupproject.model.entities.postgre.Slot;
import com.makingscience.levelupproject.model.enums.Type;
import com.makingscience.levelupproject.model.params.ReservationRequest;
import org.springframework.transaction.annotation.Transactional;

public interface ReservationFacade {

    @Transactional
    ReservationDTO add(ReservationRequest param);

    Type getType();


    @Transactional
    void cancel(Long reservationId);

    ReservationDetails getDetails(Long id);
}
