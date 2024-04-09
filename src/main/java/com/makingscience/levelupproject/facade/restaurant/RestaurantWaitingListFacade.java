package com.makingscience.levelupproject.facade.restaurant;

import com.makingscience.levelupproject.facade.interfaces.WaitingListFacade;
import com.makingscience.levelupproject.model.WaitinListNotification;
import com.makingscience.levelupproject.model.WaitingListDTO;
import com.makingscience.levelupproject.model.details.request.ReservationRequestDetails;
import com.makingscience.levelupproject.model.details.request.RestaurantReservationRequestDetails;
import com.makingscience.levelupproject.model.details.reservation.RestaurantReservationDetails;
import com.makingscience.levelupproject.model.details.slot.RestaurantSlotDetails;
import com.makingscience.levelupproject.model.entities.postgre.*;
import com.makingscience.levelupproject.model.enums.ReservationStatus;
import com.makingscience.levelupproject.model.enums.Type;
import com.makingscience.levelupproject.model.enums.WaitingStatus;
import com.makingscience.levelupproject.model.params.WaitingListRequest;
import com.makingscience.levelupproject.service.*;
import com.makingscience.levelupproject.utils.JsonUtils;
import com.makingscience.levelupproject.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class RestaurantWaitingListFacade implements WaitingListFacade {

    public static final ZoneId ZONE_ID = ZoneId.of("Asia/Tbilisi");
    private final SlotService slotService;
    private final JwtUtils jwtUtils;
    private final JsonUtils jsonUtils;
    private final ReservationService reservationService;
    private final BranchService branchService;
    private final WaitingListService waitingListService;
    private final EmailService emailService;

    @Override
    public WaitingListDTO add(WaitingListRequest param) {
        User authenticatedUser = jwtUtils.getAuthenticatedUser();
        List<Slot> slots = slotService.findByBranchId(param.getBranchId(), Pageable.unpaged()).getContent();
        Branch branch = branchService.getById(param.getBranchId());

        RestaurantReservationRequestDetails requestDetails = getRestaurantReservationRequestDetails(param.getReservationRequestDetails());
        int preferredSlotsNumber = 0;
        for (Slot slot : slots) {
            RestaurantSlotDetails slotDetails = toSlotDetails(slot.getSlotDetails());

            if (requestDetails.getNumberOfPeople() > slotDetails.getTableCapacity() ||
                    (requestDetails.getPreferredTime().isBefore(slotDetails.getReservationStartTime()) ||

                            requestDetails.getPreferredTime().isAfter(slotDetails.getReservationEndTime()))) continue;

            preferredSlotsNumber++;

            List<Reservation> reservations = reservationService.getByStatusAndReservationDay(List.of(ReservationStatus.PENDING, ReservationStatus.CONFIRMED, ReservationStatus.CHECKED_IN), param.getReservationDay());
            if (reservations.isEmpty())
                throw new ResponseStatusException(HttpStatus.CONFLICT, "No need for waiting! You can reserve now!");

        }
        if (preferredSlotsNumber > 0) {
            WaitingList waitingList = new WaitingList();
            waitingList.setPreferredDate(param.getReservationDay());
            waitingList.setWaitingStatus(WaitingStatus.ACTIVE);
            waitingList.setBranch(branch);
            waitingList.setUser(authenticatedUser);

            setDetails(waitingList, requestDetails);
            waitingListService.save(waitingList);

            return WaitingListDTO.of(waitingList, requestDetails);
        }
        throw new ResponseStatusException(HttpStatus.CONFLICT, "No slot fits your requirement!");

    }

    private void setDetails(WaitingList waitingList, RestaurantReservationRequestDetails requestDetails) {
        try {
            String details = jsonUtils.serialize(requestDetails);
            waitingList.setWaitingListDetails(details);
        } catch (Exception e) {
            log.error("Error during reservation details serialization - {}!", e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Error during reservation details serialization - " + e + "!");
        }
    }

    private static RestaurantReservationRequestDetails getRestaurantReservationRequestDetails(ReservationRequestDetails param) {
        RestaurantReservationRequestDetails restaurantReservationRequestDetails;
        if (param instanceof RestaurantReservationRequestDetails) {
            restaurantReservationRequestDetails = (RestaurantReservationRequestDetails) param;
        } else
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Slot type should be " + Type.RESTAURANT.name());
        return restaurantReservationRequestDetails;
    }

    public RestaurantSlotDetails toSlotDetails(String slotDetails) {
        RestaurantSlotDetails details;
        try {
            details = jsonUtils.deserialize(slotDetails, RestaurantSlotDetails.class);
        } catch (Exception e) {
            log.error("Can not deserialize restaurant slot details!");
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Can not deserialize restaurant slot details!");
        }
        return details;
    }

    public RestaurantReservationRequestDetails toReservationDetails(String slotDetails) {
        RestaurantReservationRequestDetails details;
        try {
            details = jsonUtils.deserialize(slotDetails, RestaurantReservationRequestDetails.class);
        } catch (Exception e) {
            log.error("Can not deserialize restaurant reservation details!");
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Can not deserialize restaurant reservation details!");
        }
        return details;
    }

    @Override
    public Type getType() {
        return Type.RESTAURANT;
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

        List<Slot> slots = slotService.findByBranchId(waitingList.getBranch().getId(), Pageable.unpaged()).getContent();
        for (Slot slot : slots) {
            RestaurantSlotDetails slotDetails = toSlotDetails(slot.getSlotDetails());

            RestaurantReservationRequestDetails requestDetails = toReservationDetails(waitingList.getWaitingListDetails());
            if (requestDetails.getNumberOfPeople() > slotDetails.getTableCapacity() ||
                    (requestDetails.getPreferredTime().isBefore(slotDetails.getReservationStartTime()) ||
                            requestDetails.getPreferredTime().isAfter(slotDetails.getReservationEndTime()))) continue;


            List<Reservation> reservations = reservationService.getByStatusAndReservationDay(List.of(ReservationStatus.PENDING, ReservationStatus.CONFIRMED, ReservationStatus.CHECKED_IN), waitingList.getPreferredDate());
            if (reservations.isEmpty()) {
                WaitinListNotification notification = createWaitingListNotification(waitingList);
                emailService.send(notification);
            }

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
}
