package com.makingscience.levelupproject.model.details.slot;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantSlotDetails extends SlotDetails {

    @NotNull(message = "tableCapacity-is required!")
    private Integer tableCapacity;

    @JsonFormat(pattern = "HH:mm")
    @NotNull(message = "reservationStartTime-is required!")
    private LocalTime reservationStartTime;

    @JsonFormat(pattern = "HH:mm")
    @NotNull(message = "reservationEndTime-is required!")
    private LocalTime reservationEndTime;
    @NotNull(message = "paidCancelledHours-is required!")
    private Integer paidCancelledHours;




}