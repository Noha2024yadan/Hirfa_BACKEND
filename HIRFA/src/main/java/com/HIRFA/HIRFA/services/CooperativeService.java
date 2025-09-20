package com.HIRFA.HIRFA.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.HIRFA.HIRFA.entity.Cooperative;
import com.HIRFA.HIRFA.repository.CooperativeRepository;

@Service
public class CooperativeService {

    @Autowired
    private CooperativeRepository cooperativeRepository;

    @Autowired
    private EmailService emailService;



    public Cooperative registerCooperative(Cooperative cooperative) {
        cooperative.setConfirmed(false); 
        cooperative.setDateCreation(LocalDateTime.now());
        cooperative.setConfirmationCode(UUID.randomUUID().toString());

        Cooperative saved = cooperativeRepository.save(cooperative);

        // Envoi email
        emailService.sendConfirmationEmail(saved.getEmail(), saved.getConfirmationCode());

        return saved;
    }


    // Confirmer email
    public boolean confirmEmail(String code) {
        Cooperative coop = cooperativeRepository.findByConfirmationCode(code);
        if (coop == null) return false;

        coop.setConfirmed(true);
        coop.setConfirmationCode(null); 
        cooperativeRepository.save(coop);
        return true;
    }

}
