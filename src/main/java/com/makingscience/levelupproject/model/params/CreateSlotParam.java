package com.makingscience.levelupproject.model.params;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.makingscience.levelupproject.model.details.slot.RestaurantSlotDetails;
import com.makingscience.levelupproject.model.details.slot.SalonSlotDetails;
import com.makingscience.levelupproject.model.details.slot.SlotDetails;
import com.makingscience.levelupproject.model.enums.Type;
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
    @NotNull(message = "name-Is required")
    private String name;
    @NotNull(message = "reserveFee-Is required")
    private Double reserveFee;
    @NotNull(message = "slotDetails-Is required")
    private SlotDetails slotDetails;



}
