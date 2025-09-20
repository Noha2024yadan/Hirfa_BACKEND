package com.HIRFA.HIRFA.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendConfirmationEmail(String to, String confirmationCode) {
        String link = "http://localhost:8080/api/cooperative/confirm?code=" + confirmationCode;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Confirmez votre compte Cooperative");
        message.setText("Bonjour, cliquez sur ce lien pour confirmer votre compte : " + link);

        mailSender.send(message);
    }

}
