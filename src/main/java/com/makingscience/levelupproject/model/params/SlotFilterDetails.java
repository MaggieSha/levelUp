package com.makingscience.levelupproject.model.params;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type",visible = true,include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = RestaurantSlotFilterDetails.class, name = "RESTAURANT"),
        @JsonSubTypes.Type(value = VisitSlotFilterDetails.class, name = "VISIT")

})
@Getter
@Setter
@NoArgsConstructor
public class SlotFilterDetails {
    private String type;

}
