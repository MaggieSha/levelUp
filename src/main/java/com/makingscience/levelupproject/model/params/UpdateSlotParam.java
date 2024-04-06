package com.makingscience.levelupproject.model.params;

import com.makingscience.levelupproject.model.details.slot.SlotDetails;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Setter
@NoArgsConstructor
@Getter
@ToString
public class UpdateSlotParam {

    @NotNull(message = "id-Is required")
    private Long id;
    private String externalId;
    private String name;
    private Double reserveFee;
    private SlotDetails slotDetails;
}
