package com.HIRFA.HIRFA.service;

import com.HIRFA.HIRFA.entity.Client;
import com.HIRFA.HIRFA.entity.PasswordResetToken;
import com.HIRFA.HIRFA.repository.ClientRepository;
import com.HIRFA.HIRFA.repository.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void initiatePasswordReset(String email) {
        Client client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Aucun compte trouvé avec cet email"));

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setClient(client);
        resetToken.setToken(token);

        emailService.sendResetPasswordEmail(email, token);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token invalide"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Le token a expiré");
        }

        if (resetToken.isUsed()) {
            throw new RuntimeException("Ce token a déjà été utilisé");
        }

        Client client = resetToken.getClient();
        client.setMotDePasse(passwordEncoder.encode(newPassword));
        resetToken.setUsed(true);

        clientRepository.save(client);
    }
}