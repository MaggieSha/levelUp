package com.makingscience.levelupproject.facade.interfaces;

import com.makingscience.levelupproject.model.ReservationDTO;
import com.makingscience.levelupproject.model.WaitingListDTO;
import com.makingscience.levelupproject.model.enums.Type;
import com.makingscience.levelupproject.model.params.ReservationRequest;
import com.makingscience.levelupproject.model.params.WaitingListRequest;
import org.springframework.transaction.annotation.Transactional;

public interface WaitingListFacade {
    @Transactional
    WaitingListDTO add(WaitingListRequest param);

    Type getType();

    void updateWaitingList(Long id);
}
