package com.makingscience.levelupproject.controller;

import com.makingscience.levelupproject.facade.routers.ReservationFacadeRouter;
import com.makingscience.levelupproject.model.ReservationDTO;
import com.makingscience.levelupproject.model.params.ReservationRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ReservationController {
    private final ReservationFacadeRouter reservationFacadeRouter;


    // TODO: get all reservations by slot, branch,merchant,id

    @PostMapping
    public ResponseEntity<ReservationDTO> add(@Valid @RequestBody ReservationRequest param) {
        ReservationDTO reservationDTO = reservationFacadeRouter.add(param);
        return ResponseEntity.ok(reservationDTO);
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        reservationFacadeRouter.cancel(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // get all my reservations

}
