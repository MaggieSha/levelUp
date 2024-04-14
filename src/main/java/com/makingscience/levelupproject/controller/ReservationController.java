package com.makingscience.levelupproject.controller;

import com.makingscience.levelupproject.facade.routers.ReservationFacadeRouter;
import com.makingscience.levelupproject.model.dto.ReservationDTO;
import com.makingscience.levelupproject.model.details.reservation.ReservationDetails;
import com.makingscience.levelupproject.model.enums.ReservationStatus;
import com.makingscience.levelupproject.model.params.ReservationRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ReservationController {
    private final ReservationFacadeRouter reservationFacadeRouter;



    @PostMapping
//    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<ReservationDTO> makeReservation(@Valid @RequestBody ReservationRequest param) {
        ReservationDTO reservationDTO = reservationFacadeRouter.add(param);
        return ResponseEntity.ok(reservationDTO);
    }

    @PostMapping("/cancel/{id}")
//    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        reservationFacadeRouter.cancel(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping
//    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<Page<ReservationDTO>> getUserReservations(Pageable pageable) {
        Page<ReservationDTO> dtos = reservationFacadeRouter.getUserReservations(pageable);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<ReservationDetails> getReservationDetails(@PathVariable Long id) {
        ReservationDetails reservationDetails = reservationFacadeRouter.getUserReservationDetails(id);
        return ResponseEntity.ok(reservationDetails);
    }

    // ADMIN ENDPOINTS
    @PutMapping()
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Void> updateStatus(@RequestParam Long id, @RequestParam ReservationStatus status) {
        reservationFacadeRouter.updateStatus(id, status);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @GetMapping("/merchant")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Page<ReservationDTO>> getMerchantReservations(@RequestParam UUID merchantId, Pageable pageable) {
        Page<ReservationDTO> dtos = reservationFacadeRouter.getMerchantReservations(merchantId,pageable);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/branch")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Page<ReservationDTO>> getBranchReservations(@RequestParam UUID branchId, Pageable pageable) {
        Page<ReservationDTO> dtos = reservationFacadeRouter.getBranchReservations(branchId,pageable);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}/admin")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<ReservationDetails> getReservationsDetailsById(@PathVariable Long id) {
        ReservationDetails details = reservationFacadeRouter.getReservationById(id);
        return ResponseEntity.ok(details);
    }
}
