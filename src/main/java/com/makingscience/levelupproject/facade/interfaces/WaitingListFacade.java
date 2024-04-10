package com.makingscience.levelupproject.facade.interfaces;

import com.makingscience.levelupproject.model.dto.WaitingListDTO;
import com.makingscience.levelupproject.model.enums.Type;
import com.makingscience.levelupproject.model.params.WaitingListRequest;
import org.springframework.transaction.annotation.Transactional;

public interface WaitingListFacade {
    @Transactional
    WaitingListDTO add(WaitingListRequest param);

    Type getType();

    void updateWaitingList(Long id);
}
