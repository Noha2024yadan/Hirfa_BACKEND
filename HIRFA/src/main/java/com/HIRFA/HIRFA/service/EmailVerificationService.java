package com.HIRFA.HIRFA.service;

import com.HIRFA.HIRFA.entity.Client;
import com.HIRFA.HIRFA.entity.EmailVerificationToken;
import com.HIRFA.HIRFA.repository.EmailVerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class EmailVerificationService {

    @Autowired
    private EmailVerificationTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    public void createVerificationToken(Client client) {
        String token = UUID.randomUUID().toString();
        EmailVerificationToken verificationToken = new EmailVerificationToken();
        verificationToken.setToken(token);
        verificationToken.setClient(client);
        verificationToken.setExpiryDate(LocalDateTime.now().plusHours(24));

        tokenRepository.save(verificationToken);
        emailService.sendVerificationEmail(client.getEmail(), token);
    }

    @Transactional
    public boolean verifyEmail(String token) {
        EmailVerificationToken verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token de vérification invalide"));

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(verificationToken);
            throw new RuntimeException("Le token de vérification a expiré");
        }

        if (verificationToken.isVerified()) {
            throw new RuntimeException("Ce token a déjà été utilisé");
        }

        Client client = verificationToken.getClient();
        client.setEmailVerified(true);
        verificationToken.setVerified(true);

        return true;
    }

    public boolean isEmailVerified(Client client) {
        return client.isEmailVerified();
    }
}