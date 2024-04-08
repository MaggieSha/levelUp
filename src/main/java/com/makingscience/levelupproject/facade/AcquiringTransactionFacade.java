package com.makingscience.levelupproject.facade;

import com.makingscience.levelupproject.service.AcquiringTransactionService;
import com.makingscience.levelupproject.service.ReservationService;
import com.makingscience.levelupproject.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AcquiringTransactionFacade {
    private final AcquiringTransactionService acquiringTransactionService;
    private final ReservationService reservationService;
    private final JwtUtils jwtUtils;




}
