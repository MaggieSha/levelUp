package com.makingscience.levelupproject.model.details.reservation;

import com.makingscience.levelupproject.model.enums.SalonService;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class SalonReservationDetails extends ReservationDetails{


    private SalonService serviceName;
    private String stylistName;
}
