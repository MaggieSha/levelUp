package com.makingscience.levelupproject.model.details.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantReservationRequestDetails implements ReservationRequestDetails {

    private Integer numberOfPeople;
}
