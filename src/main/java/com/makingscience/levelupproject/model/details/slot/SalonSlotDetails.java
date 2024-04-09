package com.makingscience.levelupproject.model.details.slot;

import com.makingscience.levelupproject.model.enums.SalonService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SalonSlotDetails extends SlotDetails {
    private SalonService serviceName;
    private String stylistName;
    private Integer visitHour;
}
