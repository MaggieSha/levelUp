package com.makingscience.levelupproject.facade.salon;

import com.makingscience.levelupproject.facade.interfaces.WaitingListFacade;
import com.makingscience.levelupproject.model.WaitinListNotification;
import com.makingscience.levelupproject.model.dto.WaitingListDTO;
import com.makingscience.levelupproject.model.details.request.SalonReservationRequestDetails;
import com.makingscience.levelupproject.model.details.slot.SalonSlotDetails;
import com.makingscience.levelupproject.model.entities.postgre.*;
import com.makingscience.levelupproject.model.enums.Type;
import com.makingscience.levelupproject.model.enums.WaitingStatus;
import com.makingscience.levelupproject.model.params.WaitingListRequest;
import com.makingscience.levelupproject.service.*;
import com.makingscience.levelupproject.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class SalonWaitingListFacade implements WaitingListFacade {
    public static final ZoneId ZONE_ID = ZoneId.of("Asia/Tbilisi");
    private final SlotService slotService;
    private final JwtUtils jwtUtils;
    private final BranchService branchService;
    private final WaitingListService waitingListService;
    private final EmailService emailService;

    @Override
    public WaitingListDTO add(WaitingListRequest param) {
        User authenticatedUser = jwtUtils.getAuthenticatedUser();

        Branch branch = branchService.getById(param.getBranchId());

        SalonReservationRequestDetails requestDetails = (SalonReservationRequestDetails) param.getReservationRequestDetails();


        List<Slot> slots = slotService.getAvailableSlotsForSalon(requestDetails.getServiceName(),requestDetails.getStylistName(),requestDetails.getPreferredTime(), param.getReservationDay(), param.getBranchId());
        if(!slots.isEmpty()) throw new ResponseStatusException(HttpStatus.CONFLICT, "No need for waiting! You can reserve now!");

        slots = slotService.findByBranchId(param.getBranchId(), Pageable.unpaged()).getContent();
        int preferredSlotsNumber = 0;
        for (Slot slot : slots) {
            SalonSlotDetails slotDetails = (SalonSlotDetails) slot.getSlotDetails();


            if (!requestDetails.getStylistName().equals(slotDetails.getStylistName())
                    || !requestDetails.getPreferredTime().equals(slotDetails.getVisitHour())
                    || !requestDetails.getServiceName().equals(slotDetails.getServiceName())) continue;

            preferredSlotsNumber++;

        }
        if (preferredSlotsNumber > 0) {
            WaitingList waitingList = new WaitingList();
            waitingList.setPreferredDate(param.getReservationDay());
            waitingList.setWaitingStatus(WaitingStatus.ACTIVE);
            waitingList.setBranch(branch);
            waitingList.setUser(authenticatedUser);

            waitingList.setWaitingListDetails(requestDetails);
            waitingListService.save(waitingList);

            return WaitingListDTO.of(waitingList, requestDetails);
        }
        throw new ResponseStatusException(HttpStatus.CONFLICT, "No slot fits your requirement!");

    }


    @Override
    public void updateWaitingList(Long id) {
        WaitingList waitingList = waitingListService.getByIdAndStatus(id, WaitingStatus.ACTIVE);

        if (waitingList.getPreferredDate().isBefore(LocalDate.now(ZONE_ID)) ||
                (waitingList.getPreferredDate().equals(LocalDate.now(ZONE_ID)) &&
                        waitingList.getPreferredTime().isBefore(LocalTime.now(ZONE_ID)))) {
            waitingList.setWaitingStatus(WaitingStatus.EXPIRED);
            waitingListService.save(waitingList);
            return;
        }
        SalonReservationRequestDetails requestDetails = (SalonReservationRequestDetails) waitingList.getWaitingListDetails();

        List<Slot> slots = slotService.getAvailableSlotsForSalon(requestDetails.getServiceName(),requestDetails.getStylistName(),requestDetails.getPreferredTime(), waitingList.getPreferredDate(), waitingList.getBranch().getId());

        if (!slots.isEmpty()) {
            WaitinListNotification notification = createWaitingListNotification(waitingList);
            emailService.send(notification);
        }

    }

    private WaitinListNotification createWaitingListNotification(WaitingList waitingList) {
        WaitinListNotification notification = new WaitinListNotification();
        notification.setBranchName(waitingList.getBranch().getName());
        notification.setPreferredTime(waitingList.getPreferredDate());
        notification.setTargetEmail(waitingList.getUser().getEmail());
        notification.setWaitingListId(waitingList.getId());
        return notification;
    }

    @Override
    public Type getType() {
        return Type.SALON;
    }
}
