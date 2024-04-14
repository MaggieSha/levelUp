package com.makingscience.levelupproject.model.details.reservation;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = RestaurantReservationDetails.class, name = "RESTAURANT"),
        @JsonSubTypes.Type(value = VisitReservationDetails.class, name = "VISIT")
})
@Getter
@Setter
@NoArgsConstructor
public class ReservationDetails {
}
