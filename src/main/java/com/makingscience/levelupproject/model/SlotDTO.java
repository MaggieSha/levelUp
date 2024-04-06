package com.makingscience.levelupproject.model;

import com.makingscience.levelupproject.model.details.slot.SlotDetails;
import com.makingscience.levelupproject.model.entities.postgre.Slot;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Setter
@NoArgsConstructor
@Getter
@ToString
@AllArgsConstructor
public class SlotDTO {
    private Long id;
    private String name;
    private String externalId;
    private Double reserveFee;
    private SlotDetails slotDetails;


    public static SlotDTO of(Slot slot,SlotDetails slotDetails) {
        return new SlotDTO(slot.getId(),slot.getName(),slot.getExternalId(),slot.getReserveFee(),slotDetails);
    }


}
