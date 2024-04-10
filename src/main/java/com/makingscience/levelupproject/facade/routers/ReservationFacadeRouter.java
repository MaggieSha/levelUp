package com.makingscience.levelupproject.facade.routers;


import com.makingscience.levelupproject.facade.interfaces.ReservationFacade;
import com.makingscience.levelupproject.model.dto.ReservationDTO;
import com.makingscience.levelupproject.model.details.reservation.ReservationDetails;
import com.makingscience.levelupproject.model.entities.postgre.Branch;
import com.makingscience.levelupproject.model.entities.postgre.Reservation;
import com.makingscience.levelupproject.model.entities.postgre.User;
import com.makingscience.levelupproject.model.enums.ReservationStatus;
import com.makingscience.levelupproject.model.params.ReservationRequest;
import com.makingscience.levelupproject.service.*;
import com.makingscience.levelupproject.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReservationFacadeRouter {
    private final List<ReservationFacade> reservationFacades;
    private final BranchService branchService;
    private final ReservationService reservationService;
    private final JwtUtils jwtUtils;
    private final AcquiringTransactionService acquiringTransactionService;
    private final PaymentTransactionService paymentTransactionService;

    public ReservationDTO add(ReservationRequest param) {
        return chooseFacade(param.getBranchId()).add(param);


    }


    public void cancel(Long id) {
        Reservation reservation = reservationService.getById(id);

        if (!List.of(ReservationStatus.PENDING, ReservationStatus.CONFIRMED).contains(reservation.getReservationStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You can not cancel reservation with status other than PENDING,CONFIRMED!");
        }
        chooseFacade(reservation.getSlot().getBranch().getId()).cancel(id);


    }

    public void updateStatus(Long id, ReservationStatus status) {

        Reservation reservation = reservationService.getById(id);

        if (!List.of(ReservationStatus.CANCELLED, ReservationStatus.CHECKED_IN, ReservationStatus.NO_SHOW, ReservationStatus.COMPLETED)
                .contains(status)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Reservation status can not be changed to status other than" +
                    "REJECTED, CHECK_IN,NO_SHOW,COMPLETED");
        }
        ReservationStatus currentStatus = reservation.getReservationStatus();


        switch (currentStatus) {
            case CONFIRMED:
                if (status.equals(ReservationStatus.CANCELLED)) {
                    acquiringTransactionService.refund(reservation.getId());
                    reservation.setReservationStatus(ReservationStatus.CANCELLED);
                    reservationService.save(reservation);
                    break;

                } else {
                    paymentTransactionService.pay(reservation.getId());
                    reservation.setReservationStatus(status);
                    reservationService.save(reservation);
                    break;
                }


            case CHECKED_IN:
                if (status.equals(ReservationStatus.COMPLETED)) {
                    reservation.setReservationStatus(status);
                    reservationService.save(reservation);
                    break;

                } else throw new ResponseStatusException(HttpStatus.FORBIDDEN);

            default:
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }


    }

    public Page<ReservationDTO> getUserReservations(Pageable pageable) {
        User authenticatedUser = jwtUtils.getAuthenticatedUser();
        Page<Reservation> reservations = reservationService.getByUser(authenticatedUser.getId(),pageable);
        List<ReservationDTO> dtos = reservations.getContent().stream().map(reservation -> ReservationDTO.of(reservation, null)).collect(Collectors.toList());
        return new PageImpl<>(dtos,pageable,reservations.getTotalElements());
    }

    public ReservationDetails getUserReservationDetails(Long id) {
        User authenticatedUser = jwtUtils.getAuthenticatedUser();
        Reservation reservation = reservationService.getByIdAndUser(id, authenticatedUser.getId());
        return chooseFacade(reservation.getSlot().getBranch().getId()).getDetails(id);
    }

    public Page<ReservationDTO> getBranchReservations(UUID branchId, Pageable pageable) {
        Page<Reservation> reservations = reservationService.getByBranchId(branchId,pageable);
        List<ReservationDTO> dtos = reservations.getContent().stream().map(reservation -> ReservationDTO.of(reservation, null)).collect(Collectors.toList());
        return new PageImpl<>(dtos,pageable,reservations.getTotalElements());
    }

    public ReservationDetails getReservationById(Long reservationId) {
        Reservation reservation = reservationService.getById(reservationId);
        return chooseFacade(reservation.getSlot().getBranch().getId()).getDetails(reservationId);
    }

    public Page<ReservationDTO> getMerchantReservations(UUID merchantId, Pageable pageable) {
        Page<Reservation> reservations = reservationService.getByMerchantId(merchantId,pageable);
        List<ReservationDTO> dtos = reservations.getContent().stream().map(reservation -> ReservationDTO.of(reservation, null)).collect(Collectors.toList());
        return new PageImpl<>(dtos,pageable,reservations.getTotalElements());
    }


    private ReservationFacade chooseFacade(UUID branchId) {
        Branch branch = branchService.getById(branchId);
        return reservationFacades.stream().filter(reservationFacade -> reservationFacade.getType().name().equals(branch.getMerchant().getCategory().getName()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not supported category type: " + branch.getMerchant().getCategory().getName()));
    }
}
