package com.makingscience.levelupproject.controller;

import com.makingscience.levelupproject.facade.RatingFacade;
import com.makingscience.levelupproject.model.ReservationDTO;
import com.makingscience.levelupproject.model.params.RatingParam;
import com.makingscience.levelupproject.model.params.ReservationRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/rating")
@RequiredArgsConstructor
@Validated
@Slf4j
public class RatingController {
    private final RatingFacade ratingFacade;
    @PostMapping
    public ResponseEntity<Void> add(@Valid @RequestBody RatingParam param) {
         ratingFacade.add(param);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/merchant/{merchantId}")
    public ResponseEntity<BigDecimal> getMerchantRating(@PathVariable UUID merchantId) {
        BigDecimal rating = ratingFacade.getMerchantRating(merchantId);
        return ResponseEntity.ok(rating);
    }

    @GetMapping("/branch/{branchId}")
    public ResponseEntity<BigDecimal> getBranchRating(@PathVariable UUID branchId) {
        BigDecimal rating = ratingFacade.getBranchRating(branchId);
        return ResponseEntity.ok(rating);
    }

}
