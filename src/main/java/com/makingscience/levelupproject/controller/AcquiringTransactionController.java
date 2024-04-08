package com.makingscience.levelupproject.controller;

import com.makingscience.levelupproject.facade.AcquiringTransactionFacade;
import com.makingscience.levelupproject.model.AcquiringTransactionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AcquiringTransactionController {

    private final AcquiringTransactionFacade acquiringTransactionFacade;


}
