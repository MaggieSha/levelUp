package com.makingscience.levelupproject.service;

import com.makingscience.levelupproject.model.WaitinListNotification;
import com.makingscience.levelupproject.model.entities.postgre.WaitingList;
import com.makingscience.levelupproject.model.enums.WaitingStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {


    private final JavaMailSender sender;
    private final WaitingListService waitingListService;


    @Async
    public void send(String email, String otp) {
        System.out.println(Thread.currentThread().getName());
        try {
            sendMail(email, otp);
        } catch (Exception e) {
            log.error("Exception sending mail", e);
        }
    }

    @Async
    public void send(WaitinListNotification notification) {
        try {
            WaitingList waitingList = waitingListService.getById(notification.getWaitingListId());
            sendMail(notification);
            waitingList.setWaitingStatus(WaitingStatus.NOTIFIED);
            waitingListService.save(waitingList);
        } catch (Exception e) {
            log.error("Exception sending mail", e);
        }
    }


    private void sendMail(String email, String otp) {
        String subject = "ONE TIME PASSWORD!";

        SimpleMailMessage message = new SimpleMailMessage();
        String content = "Hello! Here is your email verification code: " + otp;
        message.setTo(email);
        message.setSubject(subject);
        message.setText(content);
        sender.send(message);

    }

    private void sendMail(WaitinListNotification notification) {
        String subject = "DO NOT MISS CHANGE TO RESERVE SPOT!";

        SimpleMailMessage message = new SimpleMailMessage();
        String content = "Hello! There are some free spots to be reserved on  " + notification.getPreferredTime() + " in " + notification.getBranchName() + ". Do not miss chance to make a reservation!";
        message.setTo(notification.getTargetEmail());
        message.setSubject(subject);
        message.setText(content);
        sender.send(message);

    }


}
