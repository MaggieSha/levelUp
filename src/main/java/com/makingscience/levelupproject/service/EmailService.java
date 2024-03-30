package com.makingscience.levelupproject.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@Transactional(propagation = Propagation.SUPPORTS)
public class EmailService {




    @Autowired
    private JavaMailSender sender;


    public void send(String email,String otp) {
        try {
            sendMail(email,otp);
        } catch (Exception e) {
            log.error("Exception sending mail", e);
        }
    }


    private void sendMail(String email,String otp){
        String subject = "ONE TIME PASSWORD!";

        SimpleMailMessage message = new SimpleMailMessage();
        String content = "Hello! Here is your email verification code: " + otp;
        message.setTo(email);
        message.setSubject(subject);
        message.setText(content);
        sender.send(message);

    }


}
