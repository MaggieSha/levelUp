package com.makingscience.levelupproject.facade.restaurant;


import com.makingscience.levelupproject.facade.interfaces.ReservationFacade;
import com.makingscience.levelupproject.model.details.slot.SlotDetails;
import com.makingscience.levelupproject.model.dto.ReservationDTO;
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
import com.makingscience.levelupproject.utils.JwtUtils;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantReserveFacade implements ReservationFacade {

    private final SlotService slotService;
    private final JwtUtils jwtUtils;
    private final ReservationService reservationService;
    private final AcquiringTransactionService acquiringTransactionService;
    private final PaymentTransactionService paymentTransactionService;
    public static final ZoneId ZONE_ID = ZoneId.of("Asia/Tbilisi");


    @Override
    public ReservationDTO add(ReservationRequest param) {
        User authenticatedUser = jwtUtils.getAuthenticatedUser();

        RestaurantReservationRequestDetails requestDetails = (RestaurantReservationRequestDetails) param.getReservationRequestDetails();

        validateParam(requestDetails);

        List<Slot> slots = slotService.getAvailableSlotsForRestaurant(requestDetails.getNumberOfPeople(), param.getReservationDay(), param.getBranchId());
        slots =  slots.stream().filter(slot -> {
            RestaurantSlotDetails slotDetails = (RestaurantSlotDetails) slot.getSlotDetails();
            return !(slotDetails.getReservationEndTime().isBefore(requestDetails.getPreferredTime()) || slotDetails.getReservationStartTime().isAfter(requestDetails.getPreferredTime()));

        }).collect(Collectors.toList());

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

        reservation.setReservationDetails(reservationDetails);
        reservationService.save(reservation);

        acquiringTransactionService.pay(reservation);

        return ReservationDTO.of(reservation, reservationDetails);
    }




    @Override
    public void cancel(Long reservationId) {
        User authenticatedUser = jwtUtils.getAuthenticatedUser();
        Reservation reservation = reservationService.getByIdAndUser(reservationId, authenticatedUser.getId());

        RestaurantSlotDetails slotDetails = (RestaurantSlotDetails) reservation.getSlot().getSlotDetails();


        LocalDateTime reservationTime = LocalDateTime.of(reservation.getReservationDay(), reservation.getReservationTime());
        if (reservationTime.minusHours(slotDetails.getPaidCancelledHours()).isAfter(LocalDateTime.now(ZONE_ID))) {
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
        return reservation.getReservationDetails();
    }
    @Override
    public Type getType() {
        return Type.RESTAURANT;
    }
    private static void validateParam(RestaurantReservationRequestDetails requestDetails) {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<RestaurantReservationRequestDetails>> violations = validator.validate(requestDetails);
        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder();
            for (ConstraintViolation<RestaurantReservationRequestDetails> v : violations) {
                errorMessage.append(v.getMessage()).append("; ");
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage.toString());

        }
    }
}
