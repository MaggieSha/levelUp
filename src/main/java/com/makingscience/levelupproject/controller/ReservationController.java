package com.makingscience.levelupproject.controller;

import com.makingscience.levelupproject.facade.ReservationFacade;
import com.makingscience.levelupproject.model.ReservationDTO;
import com.makingscience.levelupproject.model.params.ReservationRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ReservationController {
    private final ReservationFacade reservationFacade;


    // TODO: get all reservations by slot, branch,merchant,id

    @PostMapping
    public ResponseEntity<ReservationDTO> add(@Valid @RequestBody ReservationRequest param) {
        ReservationDTO reservationDTO = reservationFacade.add(param);
        return ResponseEntity.ok(reservationDTO);
    }

}
