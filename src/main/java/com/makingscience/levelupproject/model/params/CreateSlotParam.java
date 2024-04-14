package com.makingscience.levelupproject.model.params;

import com.makingscience.levelupproject.model.details.slot.SlotDetails;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Setter
@NoArgsConstructor
@Getter
@ToString

public class CreateSlotParam {
    @NotNull(message = "branchId-Is required")
    private UUID branchId;
    @NotNull(message = "externalId-Is required")
    private String externalId;
//    @NotNull(message = "name-Is required")
    private String name;
    @NotNull(message = "reserveFee-Is required")
    private Double reserveFee;
    @NotNull(message = "slotDetails-Is required")
    private SlotDetails slotDetails;



}
