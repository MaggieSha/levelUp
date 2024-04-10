package com.makingscience.levelupproject.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.makingscience.levelupproject.model.details.request.ReservationRequestDetails;
import com.makingscience.levelupproject.model.entities.postgre.WaitingList;
import com.makingscience.levelupproject.model.enums.WaitingStatus;
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
public class WaitingListDTO {


    private Long id;

    private UUID branchId;

    private UUID userId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate preferredDate;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime preferredTime;

    private WaitingStatus waitingStatus;

    private ReservationRequestDetails reservationDetails;

    public static WaitingListDTO of(WaitingList waitingList, ReservationRequestDetails details) {
        WaitingListDTO dto = new WaitingListDTO();
        dto.setId(waitingList.getId());
        dto.setBranchId(waitingList.getBranch().getId());
        dto.setUserId(waitingList.getUser().getId());
        dto.setWaitingStatus(waitingList.getWaitingStatus());
        dto.setPreferredDate(waitingList.getPreferredDate());
        dto.setPreferredTime(waitingList.getPreferredTime());
        dto.setReservationDetails(details);
        return dto;
    }
}
