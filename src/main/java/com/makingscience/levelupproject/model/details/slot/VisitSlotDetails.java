package com.makingscience.levelupproject.model.details.slot;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VisitSlotDetails extends SlotDetails {
    @NotNull(message = "serviceName-Is required")
    private String serviceName;



    @JsonFormat(pattern = "HH:mm")
    @NotNull(message = "visitHour-is required!")
    private LocalTime visitHour;

    @NotNull(message = "paidCancelledHours-is required!")
    private Integer paidCancelledHours;
}

