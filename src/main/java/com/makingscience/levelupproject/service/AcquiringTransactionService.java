package com.makingscience.levelupproject.service;

import com.makingscience.levelupproject.model.entities.postgre.AcquiringTransaction;
import com.makingscience.levelupproject.model.entities.postgre.Reservation;
import com.makingscience.levelupproject.model.enums.AcquiringTransactionStatus;
import com.makingscience.levelupproject.repository.AcquiringTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AcquiringTransactionService {
    private final AcquiringTransactionRepository acquiringTransactionRepository;

    public AcquiringTransaction save(AcquiringTransaction transaction) {
        return acquiringTransactionRepository.save(transaction);
    }

    public AcquiringTransaction getById(Long transactionId) {
        return acquiringTransactionRepository.findById(transactionId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Acquiring transaction with id " + transactionId + " not found!"));
    }


    public void pay(Reservation reservation) {
        AcquiringTransaction transaction = new AcquiringTransaction();
        transaction.setAcquiringTransactionStatus(AcquiringTransactionStatus.SUCCESSFUL);
        transaction.setReservation(reservation);
        transaction.setAmount(reservation.getSlot().getReserveFee());
        acquiringTransactionRepository.save(transaction);
    }

    public void refund(Long reservationId) {

        AcquiringTransaction transaction = acquiringTransactionRepository.getByReservationId(reservationId).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Acquiring transaction for reservation with id " + reservationId + " not found!"));

        if (!transaction.getAcquiringTransactionStatus().equals(AcquiringTransactionStatus.REFUNDED))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Transaction with id " + transaction.getId() + "is already refunded!");

        transaction.setAcquiringTransactionStatus(AcquiringTransactionStatus.REFUNDED);
        acquiringTransactionRepository.save(transaction);

    }


}
