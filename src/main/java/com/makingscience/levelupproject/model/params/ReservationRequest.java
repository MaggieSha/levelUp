package com.makingscience.levelupproject.model.params;

import com.makingscience.levelupproject.model.details.request.ReservationRequestDetails;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;


@NoArgsConstructor
@Setter
@Getter
public class ReservationRequest {

    private Long slotId;

    private LocalDate reservationDay;
    private LocalTime reservationTime;

    private ReservationRequestDetails reservationRequestDetails;




}
