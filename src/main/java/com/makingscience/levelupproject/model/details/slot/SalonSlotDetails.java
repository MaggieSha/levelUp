package com.makingscience.levelupproject.model.details.slot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SalonSlotDetails extends SlotDetails {
    private String serviceName;
    private String stylistName;
}
