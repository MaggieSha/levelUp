package com.makingscience.levelupproject.model.params;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@NoArgsConstructor
@Getter
public class VisitSlotFilterDetails extends SlotFilterDetails {
    private LocalDate preferredDay;
    private LocalTime preferredTime;
    private String serviceName;
}
