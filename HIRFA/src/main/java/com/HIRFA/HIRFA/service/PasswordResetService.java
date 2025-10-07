package com.HIRFA.HIRFA.service;

import com.HIRFA.HIRFA.entity.ResetPasswordToken;
import com.HIRFA.HIRFA.repository.ClientRepository;
import com.HIRFA.HIRFA.repository.CooperativeRepository;
import com.HIRFA.HIRFA.repository.DesignerRepository;
import com.HIRFA.HIRFA.repository.ResetPasswordTokenRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    private final ClientRepository clientRepo;
    private final DesignerRepository designerRepo;
    private final CooperativeRepository cooperativeRepo;
    private final ResetPasswordTokenRepository tokenRepo;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    // recupere date de token et front url depuis le fichier applications
    @Value("${app.reset.token-expiration-minutes:15}")
    private long tokenExpiryMinutes;

    @Value("${app.frontend.reset-url}")
    private String frontendResetUrl;

    public PasswordResetService(ClientRepository clientRepo,
            DesignerRepository designerRepo,
            CooperativeRepository cooperativeRepo,
            ResetPasswordTokenRepository tokenRepo,
            JavaMailSender mailSender,
            PasswordEncoder passwordEncoder) {
        this.clientRepo = clientRepo;
        this.designerRepo = designerRepo;
        this.cooperativeRepo = cooperativeRepo;
        this.tokenRepo = tokenRepo;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
    }

    public void createPasswordResetTokenAndSendEmail(String email) {
        UUID userId = null;
        UserType type = null;

        if (clientRepo.findByEmail(email).isPresent()) {
            userId = clientRepo.findByEmail(email).get().getClientId();
            type = UserType.CLIENT;
        } else if (designerRepo.findByEmail(email).isPresent()) {
            userId = designerRepo.findByEmail(email).get().getDesignerId();
            type = UserType.DESIGNER;
        } else if (cooperativeRepo.findByEmail(email).isPresent()) {
            userId = cooperativeRepo.findByEmail(email).get().getCooperativeId();
            type = UserType.COOPERATIVE;
        }

        if (userId == null)
            return;

        String token = UUID.randomUUID().toString();
        ResetPasswordToken rToken = new ResetPasswordToken();
        rToken.setToken(token);
        rToken.setUserId(userId);
        rToken.setUserType(type);
        rToken.setExpiryDate(LocalDateTime.now().plusMinutes(tokenExpiryMinutes));
        tokenRepo.save(rToken);

        String resetLink = frontendResetUrl + "?token=" + token;

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("Réinitialisation de mot de passe");
        msg.setText("Cliquez sur ce lien pour réinitialiser votre mot de passe : " + resetLink);
        mailSender.send(msg);
    }

    public boolean resetPasswordByRole(String tokenStr, String newPassword, UserType expectedType) {
        Optional<ResetPasswordToken> tkOpt = tokenRepo.findByToken(tokenStr);
        if (tkOpt.isEmpty())
            return false;

        ResetPasswordToken tk = tkOpt.get();

        if (!tk.getUserType().equals(expectedType))
            return false;
        if (tk.getExpiryDate().isBefore(LocalDateTime.now())) {
            tokenRepo.delete(tk);
            return false;
        }

        UUID userId = tk.getUserId();
        switch (expectedType) {
            case CLIENT -> clientRepo.findById(userId).ifPresent(c -> {
                c.setMotDePasse(passwordEncoder.encode(newPassword));
                clientRepo.save(c);
            });
            case DESIGNER -> designerRepo.findById(userId).ifPresent(d -> {
                d.setMotDePasse(passwordEncoder.encode(newPassword));
                designerRepo.save(d);
            });
            case COOPERATIVE -> cooperativeRepo.findById(userId).ifPresent(coop -> {
                coop.setMotDePasse(passwordEncoder.encode(newPassword));
                cooperativeRepo.save(coop);
            });
        }

        tokenRepo.delete(tk);
        return true;
    }
}
