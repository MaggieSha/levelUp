package com.makingscience.levelupproject.model.details.slot;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = RestaurantSlotDetails.class, name = "RESTAURANT"),
        @JsonSubTypes.Type(value = SalonSlotDetails.class, name = "SALON")
})

public class SlotDetails {
}
