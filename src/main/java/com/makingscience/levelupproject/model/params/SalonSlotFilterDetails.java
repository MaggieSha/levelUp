package com.makingscience.levelupproject.model.params;

import com.makingscience.levelupproject.model.enums.SalonService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@NoArgsConstructor
@Getter
public class SalonSlotFilterDetails  extends SlotFilterDetails {
    private LocalDate preferredDay;
    private LocalTime preferredTime;
    private SalonService serviceName;
    private String stylistName;
}
