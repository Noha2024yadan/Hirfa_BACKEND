package com.HIRFA.HIRFA.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Vérification de votre compte HIRFA");
        message.setText("Pour vérifier votre compte, veuillez cliquer sur le lien suivant:\n" +
                "http://localhost:8081/api/clients/verify-email?token=" + token + "\n\n" +
                "Ce lien expirera dans 24 heures.");

        mailSender.send(message);
    }

    public void sendResetPasswordEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Réinitialisation de votre mot de passe HIRFA");
        message.setText("Pour réinitialiser votre mot de passe, veuillez cliquer sur le lien suivant:\n" +
                "http://localhost:8081/api/clients/reset-password?token=" + token + "\n\n" +
                "Ce lien expirera dans 1 heure.");

        mailSender.send(message);
    }
}