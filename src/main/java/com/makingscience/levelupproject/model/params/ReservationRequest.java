package com.makingscience.levelupproject.model.params;

import com.makingscience.levelupproject.model.details.request.ReservationRequestDetails;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@NoArgsConstructor
@Setter
@Getter
public class ReservationRequest {

    private Long slotId;

    private LocalDate reservationDay;

    private ReservationRequestDetails reservationRequestDetails;




}
