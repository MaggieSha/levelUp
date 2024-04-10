package com.makingscience.levelupproject.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.makingscience.levelupproject.model.details.slot.SlotDetails;
import com.makingscience.levelupproject.repository.FilterQueryResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SlotFilterDTO {
    private Long numberOfSlots;
    private SlotDetails slotDetails;
    private UUID branchId;

    public static SlotFilterDTO of(FilterQueryResponse slot,SlotDetails slotDetails) {
        SlotFilterDTO slotFilterDTO = new SlotFilterDTO();
        slotFilterDTO.setSlotDetails(slotDetails);
        slotFilterDTO.setNumberOfSlots(slot.getNumberOfSlots());
        slotFilterDTO.setBranchId(slot.getBranchId());
        return slotFilterDTO;
    }


}
