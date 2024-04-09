package com.makingscience.levelupproject.service;

import com.makingscience.levelupproject.model.entities.postgre.AcquiringTransaction;
import com.makingscience.levelupproject.model.entities.postgre.PaymentTransaction;
import com.makingscience.levelupproject.model.entities.postgre.Reservation;
import com.makingscience.levelupproject.model.enums.AcquiringTransactionStatus;
import com.makingscience.levelupproject.model.enums.ReservationStatus;
import com.makingscience.levelupproject.repository.AcquiringTransactionRepository;
import com.makingscience.levelupproject.repository.PaymentTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentTransactionService {
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final AcquiringTransactionRepository acquiringTransactionRepository;
    public void pay(Long id) {
        AcquiringTransaction transaction = acquiringTransactionRepository.getByReservationId(id).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Acquiring transaction for reservation with id " + id + " not found!"));
        if (!transaction.getAcquiringTransactionStatus().equals(AcquiringTransactionStatus.SUCCESSFUL))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Can not create payment transaction for refunded acquiring transaction!");
        Optional<PaymentTransaction> optionalPaymentTransaction = paymentTransactionRepository.getByReservationId(id);
        if(optionalPaymentTransaction.isPresent())throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment transaction for reservation with id " + id + " already exists!");
        Reservation reservation = transaction.getReservation();

        PaymentTransaction paymentTransaction = new PaymentTransaction();
        Double commission = calculatePercent(transaction.getAmount(),reservation.getSlot().getBranch().getMerchant().getCategory().getCommission(), RoundingMode.UP);

        paymentTransaction.setAmount(transaction.getAmount() - commission);
        paymentTransaction.setCommission(commission);
        paymentTransaction.setReservation(reservation);
        paymentTransactionRepository.save(paymentTransaction);
    }

    private Double calculatePercent(Double amount, Double percent, RoundingMode roundingMode){
        BigDecimal oneHundredBigDecimal = BigDecimal.valueOf(100.0);
        BigDecimal sentAmountBigDecimal = BigDecimal.valueOf(amount);
        BigDecimal senderCommissionPercentBigDecimal = BigDecimal.valueOf(percent);
        Double response =  sentAmountBigDecimal.multiply(senderCommissionPercentBigDecimal)
                .divide(oneHundredBigDecimal, 2, roundingMode).doubleValue();

        return response;
    }
}
