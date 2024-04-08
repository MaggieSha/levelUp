package com.makingscience.levelupproject.model.details.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantReservationRequestDetails extends ReservationRequestDetails {

    private Integer numberOfPeople;
    private LocalTime preferredTime;
}
