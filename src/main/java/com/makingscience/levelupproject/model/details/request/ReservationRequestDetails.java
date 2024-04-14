package com.makingscience.levelupproject.model.details.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type",visible = true,include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = RestaurantReservationRequestDetails.class, name = "RESTAURANT"),
        @JsonSubTypes.Type(value = VisitReservationRequestDetails.class, name = "VISIT")
})
@Getter
@Setter
@NoArgsConstructor
public class ReservationRequestDetails {
    private String type;
}
