package com.makingscience.levelupproject.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.makingscience.levelupproject.model.details.reservation.ReservationDetails;
import com.makingscience.levelupproject.model.entities.postgre.Reservation;
import com.makingscience.levelupproject.model.enums.ReservationStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;


@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReservationDTO {
    private Long id;

    private Long slotId;

    private UUID userId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate reservationDay;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime reservationTime;

    private Integer duration;

    private ReservationStatus reservationStatus;
    private  ReservationDetails reservationDetails;


    public static ReservationDTO of(Reservation reservation, ReservationDetails reservationDetails) {
        ReservationDTO dto = new ReservationDTO();
        dto.setId(reservation.getId());
        dto.setSlotId(reservation.getSlot().getId());
        dto.setUserId(reservation.getUser().getId());
        dto.setReservationDay(reservation.getReservationDay());
        dto.setReservationTime(reservation.getReservationTime());
        dto.setReservationStatus(reservation.getReservationStatus());
        dto.setReservationDetails(reservationDetails);
        return dto;
    }
}
