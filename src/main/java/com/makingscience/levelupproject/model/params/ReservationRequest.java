package com.makingscience.levelupproject.model.params;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.makingscience.levelupproject.model.details.request.ReservationRequestDetails;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;


@NoArgsConstructor
@Setter
@Getter
public class ReservationRequest {

    private UUID branchId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate reservationDay;

    private ReservationRequestDetails reservationRequestDetails;




}
