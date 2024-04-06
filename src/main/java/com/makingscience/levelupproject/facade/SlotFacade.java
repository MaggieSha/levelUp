package com.makingscience.levelupproject.facade;

import com.makingscience.levelupproject.model.SlotDTO;
import com.makingscience.levelupproject.model.details.slot.SlotDetails;
import com.makingscience.levelupproject.model.entities.postgre.Slot;
import com.makingscience.levelupproject.model.enums.Type;
import com.makingscience.levelupproject.model.params.CreateSlotParam;
import com.makingscience.levelupproject.model.params.UpdateSlotParam;

public interface SlotFacade {

    SlotDTO add(CreateSlotParam param);
    SlotDTO update(Slot slot, UpdateSlotParam param);



    Type getType();

    SlotDTO getById(Slot slot);

    SlotDetails getDetails(String slotDetails);
}
