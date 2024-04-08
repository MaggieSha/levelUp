package com.makingscience.levelupproject.model.params;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Setter
@NoArgsConstructor
@Getter
public class RestaurantSlotFilterDetails extends SlotFilterDetails{

    private Integer numberOfPeople;
    private LocalDate preferredDay;

}
