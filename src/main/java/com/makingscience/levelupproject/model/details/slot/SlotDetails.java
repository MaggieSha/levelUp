package com.makingscience.levelupproject.model.details.slot;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = RestaurantSlotDetails.class, name = "RESTAURANT"),
        @JsonSubTypes.Type(value = VisitSlotDetails.class, name = "VISIT")
})
@Getter
@Setter
@NoArgsConstructor
public class SlotDetails {
//    private String type;
}
