package com.makingscience.levelupproject.facade.salon;

import com.makingscience.levelupproject.facade.interfaces.ReservationFacade;
import com.makingscience.levelupproject.model.dto.ReservationDTO;
import com.makingscience.levelupproject.model.details.request.SalonReservationRequestDetails;
import com.makingscience.levelupproject.model.details.reservation.ReservationDetails;
import com.makingscience.levelupproject.model.details.reservation.SalonReservationDetails;
import com.makingscience.levelupproject.model.details.slot.SalonSlotDetails;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class SalonReserveFacade implements ReservationFacade {
    private final SlotService slotService;
    private final JwtUtils jwtUtils;
    private final ReservationService reservationService;
    private final AcquiringTransactionService acquiringTransactionService;
    private final PaymentTransactionService paymentTransactionService;
    public static final ZoneId ZONE_ID = ZoneId.of("Asia/Tbilisi");



    @Override
    public ReservationDTO add(ReservationRequest param) {
        User authenticatedUser = jwtUtils.getAuthenticatedUser();

        SalonReservationRequestDetails requestDetails = (SalonReservationRequestDetails) param.getReservationRequestDetails();

        validateParam(requestDetails);

        List<Slot> slots = slotService.getAvailableSlotsForSalon(requestDetails.getServiceName(),requestDetails.getStylistName(),requestDetails.getPreferredTime(), param.getReservationDay(), param.getBranchId());


        if (slots.isEmpty())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "All slots with requested properties are already reserved!");

        Reservation reservation = new Reservation();
        reservation.setReservationDay(param.getReservationDay());
        reservation.setReservationTime(requestDetails.getPreferredTime());
        reservation.setReservationStatus(ReservationStatus.CONFIRMED);
        reservation.setSlot(slots.get(0));
        reservation.setUser(authenticatedUser);

        SalonReservationDetails reservationDetails = new SalonReservationDetails();
        reservationDetails.setServiceName(requestDetails.getServiceName());
        reservationDetails.setStylistName(requestDetails.getStylistName());

        reservation.setReservationDetails(reservationDetails);
        reservationService.save(reservation);

        acquiringTransactionService.pay(reservation);

        return ReservationDTO.of(reservation, reservationDetails);
    }




    @Override
    public void cancel(Long reservationId) {
        User authenticatedUser = jwtUtils.getAuthenticatedUser();
        Reservation reservation = reservationService.getByIdAndUser(reservationId, authenticatedUser.getId());

        SalonSlotDetails slotDetails = (SalonSlotDetails) reservation.getSlot().getSlotDetails();


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
        return reservation.getReservationDetails();
    }
    @Override
    public Type getType() {
        return Type.SALON;
    }

    private static void validateParam(SalonReservationRequestDetails requestDetails) {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<SalonReservationRequestDetails>> violations = validator.validate(requestDetails);
        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder();
            for (ConstraintViolation<SalonReservationRequestDetails> v : violations) {
                errorMessage.append(v.getMessage()).append("; ");
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage.toString());

        }
    }

}
