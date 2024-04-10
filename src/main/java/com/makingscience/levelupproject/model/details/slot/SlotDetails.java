package com.makingscience.levelupproject.model.details.slot;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = RestaurantSlotDetails.class, name = "RESTAURANT"),
        @JsonSubTypes.Type(value = SalonSlotDetails.class, name = "SALON")
})
@Getter
@Setter
@NoArgsConstructor
public class SlotDetails {
//    private String type;
}
