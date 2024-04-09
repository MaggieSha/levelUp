package com.makingscience.levelupproject.facade.restaurant;


import com.makingscience.levelupproject.facade.interfaces.ReservationFacade;
import com.makingscience.levelupproject.model.ReservationDTO;
import com.makingscience.levelupproject.model.details.request.ReservationRequestDetails;
import com.makingscience.levelupproject.model.details.request.RestaurantReservationRequestDetails;
import com.makingscience.levelupproject.model.details.reservation.ReservationDetails;
import com.makingscience.levelupproject.model.details.reservation.RestaurantReservationDetails;
import com.makingscience.levelupproject.model.details.slot.RestaurantSlotDetails;
import com.makingscience.levelupproject.model.entities.postgre.Reservation;
import com.makingscience.levelupproject.model.entities.postgre.Slot;
import com.makingscience.levelupproject.model.entities.postgre.User;
import com.makingscience.levelupproject.model.enums.ReservationStatus;
import com.makingscience.levelupproject.model.enums.Type;
import com.makingscience.levelupproject.model.params.ReservationRequest;
import com.makingscience.levelupproject.service.AcquiringTransactionService;
import com.makingscience.levelupproject.service.PaymentTransactionService;
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
    private final PaymentTransactionService paymentTransactionService;
    public static final ZoneId ZONE_ID = ZoneId.of("Asia/Tbilisi");

    private static RestaurantReservationRequestDetails getRestaurantReservationRequestDetails(ReservationRequestDetails param) {
        RestaurantReservationRequestDetails restaurantReservationRequestDetails;
        if (param instanceof RestaurantReservationRequestDetails) {
            restaurantReservationRequestDetails = (RestaurantReservationRequestDetails) param;
        } else
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Slot type should be RESTAURANT");
        return restaurantReservationRequestDetails;
    }

    @Override
    public ReservationDTO add(ReservationRequest param) {
        User authenticatedUser = jwtUtils.getAuthenticatedUser();

        RestaurantReservationRequestDetails requestDetails = getRestaurantReservationRequestDetails(param.getReservationRequestDetails());
        List<Slot> slots = slotService.getAvailableSlotsForRestaurant(requestDetails.getNumberOfPeople(), param.getReservationDay(), param.getBranchId());


        if (slots.isEmpty())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "All slots with requested properties are already reserved!");

        Reservation reservation = new Reservation();
        reservation.setReservationDay(param.getReservationDay());
        reservation.setReservationTime(requestDetails.getPreferredTime());
        reservation.setReservationStatus(ReservationStatus.CONFIRMED);
        reservation.setSlot(slots.get(0));
        reservation.setUser(authenticatedUser);

        RestaurantReservationDetails reservationDetails = new RestaurantReservationDetails();
        reservationDetails.setNumberOfPeople(requestDetails.getNumberOfPeople());


        setDetails(reservationDetails, reservation);
        reservationService.save(reservation);

        acquiringTransactionService.pay(reservation);

        return ReservationDTO.of(reservation, reservationDetails);
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
    public void cancel(Long reservationId) {
        User authenticatedUser = jwtUtils.getAuthenticatedUser();
        Reservation reservation = reservationService.getByIdAndUser(reservationId, authenticatedUser.getId());


        RestaurantSlotDetails slotDetails = toDetails(reservation.getSlot().getSlotDetails());

        LocalDateTime reservationTime = LocalDateTime.of(reservation.getReservationDay(), reservation.getReservationTime());
        if (reservationTime.minusHours(slotDetails.getPaidCancelledHours()).isBefore(LocalDateTime.now(ZONE_ID))) {
            acquiringTransactionService.refund(reservation.getId());
        } else {
            paymentTransactionService.pay(reservation.getId());
        }
        reservation.setReservationStatus(ReservationStatus.CANCELLED);
        reservationService.save(reservation);

    }

    @Override
    public ReservationDetails getDetails(Long id) {
        User authenticatedUser = jwtUtils.getAuthenticatedUser();
        Reservation reservation = reservationService.getByIdAndUser(id, authenticatedUser.getId());
        RestaurantReservationDetails details = null;
        try {
            details= jsonUtils.deserialize(reservation.getReservationDetails(), RestaurantReservationDetails.class);
        } catch (Exception e) {
            log.error("Can not deserialize restaurant reservation details!");
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Can not deserialize restaurant reservation details!");
        }
        return details;
    }




}
