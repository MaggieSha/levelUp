package com.makingscience.levelupproject.model.details.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.makingscience.levelupproject.model.enums.SalonService;
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
public class SalonReservationRequestDetails extends ReservationRequestDetails{
    @JsonFormat(pattern = "HH:mm")
    @NotNull(message = "preferredTime-is required!")
    private LocalTime preferredTime;

    @NotNull(message = "serviceName-is required!")
    private SalonService serviceName;

    @NotNull(message = "stylistName-is required!")
    private String stylistName;
}
