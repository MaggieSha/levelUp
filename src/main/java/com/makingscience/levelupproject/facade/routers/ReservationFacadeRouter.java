package com.makingscience.levelupproject.facade.routers;


import com.makingscience.levelupproject.facade.interfaces.ReservationFacade;
import com.makingscience.levelupproject.model.ReservationDTO;
import com.makingscience.levelupproject.model.entities.postgre.Branch;
import com.makingscience.levelupproject.model.entities.postgre.Reservation;
import com.makingscience.levelupproject.model.entities.postgre.Slot;
import com.makingscience.levelupproject.model.enums.ReservationStatus;
import com.makingscience.levelupproject.model.params.ReservationRequest;
import com.makingscience.levelupproject.service.AcquiringTransactionService;
import com.makingscience.levelupproject.service.BranchService;
import com.makingscience.levelupproject.service.ReservationService;
import com.makingscience.levelupproject.service.SlotService;
import com.makingscience.levelupproject.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReservationFacadeRouter {
    private final List<ReservationFacade> reservationFacades;
    private final BranchService branchService;
    private final SlotService slotService;
    private final ReservationService reservationService;
    private final JwtUtils jwtUtils;
    private final AcquiringTransactionService acquiringTransactionService;

    public ReservationDTO add(ReservationRequest param) {
        Slot slot = slotService.findById(param.getSlotId());
        return chooseFacade(slot.getBranch().getId()).add(param);


    }

    private ReservationFacade chooseFacade(UUID branchId) {
        Branch branch = branchService.getById(branchId);
        return reservationFacades.stream().filter(reservationFacade -> reservationFacade.getType().name().equals(branch.getMerchant().getCategory().getName()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not supported category type: " + branch.getMerchant().getCategory().getName()));
    }

    public void cancel(Long id) {
        Reservation reservation = reservationService.getById(id);

        if (!List.of(ReservationStatus.PENDING, ReservationStatus.CONFIRMED).contains(reservation.getReservationStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You can not cancel reservation with status other than PENDING,CONFIRMED!");
        }
        chooseFacade(reservation.getSlot().getBranch().getId()).cancel(reservation);


    }
}
