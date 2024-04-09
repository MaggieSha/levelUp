package com.makingscience.levelupproject.facade.interfaces;

import com.makingscience.levelupproject.model.SlotDTO;
import com.makingscience.levelupproject.model.details.slot.SlotDetails;
import com.makingscience.levelupproject.model.entities.postgre.Slot;
import com.makingscience.levelupproject.model.enums.Type;
import com.makingscience.levelupproject.model.params.CreateSlotParam;
import com.makingscience.levelupproject.model.params.SlotFilterParam;
import com.makingscience.levelupproject.model.params.UpdateSlotParam;
import com.makingscience.levelupproject.repository.FilterQueryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SlotFacade {

    SlotDTO createSlot(CreateSlotParam param);

    SlotDTO updateSlot(Slot slot, UpdateSlotParam param);


    Type getType();

    SlotDetails getDetails(String slotDetails);



    Page<FilterQueryResponse> filter(SlotFilterParam slotFilterParam, Pageable pageable);
}
