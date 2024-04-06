package com.makingscience.levelupproject.service.interfaces;


import com.makingscience.levelupproject.model.ReservationDTO;
import com.makingscience.levelupproject.model.enums.Type;
import com.makingscience.levelupproject.model.params.ReservationRequest;
import org.springframework.transaction.annotation.Transactional;

public interface ReservationService {

    @Transactional
    ReservationDTO reserve(ReservationRequest param);

    Type getType();
}
