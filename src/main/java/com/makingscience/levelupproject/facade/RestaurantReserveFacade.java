package com.makingscience.levelupproject.facade;


import com.makingscience.levelupproject.facade.interfaces.ReservationFacade;
import com.makingscience.levelupproject.model.ReservationDTO;
import com.makingscience.levelupproject.model.details.request.ReservationRequestDetails;
import com.makingscience.levelupproject.model.details.request.RestaurantReservationRequestDetails;
import com.makingscience.levelupproject.model.details.reservation.RestaurantReservationDetails;
import com.makingscience.levelupproject.model.details.slot.RestaurantSlotDetails;
import com.makingscience.levelupproject.model.entities.postgre.Branch;
import com.makingscience.levelupproject.model.entities.postgre.Reservation;
import com.makingscience.levelupproject.model.entities.postgre.Slot;
import com.makingscience.levelupproject.model.entities.postgre.User;
import com.makingscience.levelupproject.model.enums.ReservationStatus;
import com.makingscience.levelupproject.model.enums.Type;
import com.makingscience.levelupproject.model.params.ReservationRequest;
import com.makingscience.levelupproject.service.AcquiringTransactionService;
import com.makingscience.levelupproject.service.ReservationService;
import com.makingscience.levelupproject.service.SlotService;
import com.makingscience.levelupproject.utils.JsonUtils;
import com.makingscience.levelupproject.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantReserveFacade implements ReservationFacade {

    private final SlotService slotService;
    private final JwtUtils jwtUtils;
    private final JsonUtils jsonUtils;
    private final ReservationService reservationService;
    private final AcquiringTransactionService acquiringTransactionService;
    public static final ZoneId ZONE_ID = ZoneId.of("Asia/Tbilisi");

    private static RestaurantReservationRequestDetails getRestaurantReservationRequestDetails(ReservationRequestDetails param, Branch branch) {
        RestaurantReservationRequestDetails restaurantReservationRequestDetails;
        if (param instanceof RestaurantReservationRequestDetails) {
            restaurantReservationRequestDetails = (RestaurantReservationRequestDetails) param;
        } else
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Slot type should be " + branch.getMerchant().getCategory().getName());
        return restaurantReservationRequestDetails;
    }

    @Override
    public ReservationDTO add(ReservationRequest param) {
        User authenticatedUser = jwtUtils.getAuthenticatedUser();
        Slot slot = slotService.findByIdAndLock(param.getSlotId());
        RestaurantReservationRequestDetails requestDetails = getRestaurantReservationRequestDetails(param.getReservationRequestDetails(), slot.getBranch());


        RestaurantSlotDetails slotDetails = toDetails(slot.getSlotDetails());
        if (requestDetails.getNumberOfPeople() > slotDetails.getTableCapacity()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Slot max capacity is  " + slotDetails.getTableCapacity());
        }

        if (requestDetails.getPreferredTime().isBefore(slotDetails.getReservationStartTime()) ||
                requestDetails.getPreferredTime().isAfter(slotDetails.getReservationEndTime())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You can reserve only between " + slotDetails.getReservationStartTime() + " and " + slotDetails.getReservationEndTime() + " hours.");
        }


        List<Reservation> reservations = reservationService.getByStatusAndReservationDay(List.of(ReservationStatus.PENDING, ReservationStatus.CONFIRMED, ReservationStatus.CHECKED_IN), param.getReservationDay());
        if (!reservations.isEmpty())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Slot is already reserved!");

        Reservation reservation = new Reservation();
        reservation.setReservationDay(param.getReservationDay());
        reservation.setReservationTime(requestDetails.getPreferredTime());
        reservation.setReservationStatus(ReservationStatus.CONFIRMED);
        reservation.setSlot(slot);
        reservation.setUser(authenticatedUser);

        RestaurantReservationDetails reservationDetails = new RestaurantReservationDetails();
        reservationDetails.setNumberOfPeople(requestDetails.getNumberOfPeople());


        setDetails(reservationDetails, reservation);
        reservationService.save(reservation);

        acquiringTransactionService.pay(reservation);

        return ReservationDTO.of(reservation,reservationDetails);
    }

    private void setDetails(RestaurantReservationDetails reservationDetails, Reservation reservation) {
        try {
            String details = jsonUtils.serialize(reservationDetails);
            reservation.setReservationDetails(details);
        } catch (Exception e) {
            log.error("Error during reservation details serialization - {}!", e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Error during reservation details serialization - " + e + "!");
        }
    }

    public RestaurantSlotDetails toDetails(String slotDetails) {
        RestaurantSlotDetails details;
        try {
            details = jsonUtils.deserialize(slotDetails, RestaurantSlotDetails.class);
        } catch (Exception e) {
            log.error("Can not deserialize restaurant slot details!");
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Can not deserialize restaurant slot details!");
        }
        return details;
    }

    @Override
    public Type getType() {
        return Type.RESTAURANT;
    }

    @Override
    public void cancel(Reservation reservation) {
        if (reservation.getReservationStatus().equals(ReservationStatus.CONFIRMED)) {
            User authenticatedUser = jwtUtils.getAuthenticatedUser();
            if (!reservation.getUser().getId().equals(authenticatedUser.getId())) {
                acquiringTransactionService.refund(reservation.getId());

            } else {
                RestaurantSlotDetails slotDetails = toDetails(reservation.getSlot().getSlotDetails());
                LocalDateTime reservationTime = LocalDateTime.of(reservation.getReservationDay(),reservation.getReservationTime());
                if(reservationTime.minusHours(slotDetails.getPaidCancelledHours()).isBefore(LocalDateTime.now(ZONE_ID))){
                    acquiringTransactionService.refund(reservation.getId());
                } else {
                    //create Distributor transaction
                }
            }

        }
        reservation.setReservationStatus(ReservationStatus.CANCELLED);
        reservationService.save(reservation);
    }
}
