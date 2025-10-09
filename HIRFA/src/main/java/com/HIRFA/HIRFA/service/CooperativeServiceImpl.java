package com.HIRFA.HIRFA.service;

import com.HIRFA.HIRFA.dto.CooperativeRegistrationDto;
import com.HIRFA.HIRFA.entity.Cooperative;
import com.HIRFA.HIRFA.repository.CooperativeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CooperativeServiceImpl implements CooperativeService {

    @Autowired
    private CooperativeRepository cooperativeRepository;

    @Override
    public Cooperative registerCooperative(CooperativeRegistrationDto registrationDto) {
        Cooperative cooperative = new Cooperative();
        cooperative.setUsername(registrationDto.getUsername());
        cooperative.setEmail(registrationDto.getEmail());
        cooperative.setNom(registrationDto.getNom());
        cooperative.setPrenom(registrationDto.getPrenom());
        cooperative.setTelephone(registrationDto.getTelephone());
        cooperative.setDescription(registrationDto.getDescription());
        cooperative.setAdresse(registrationDto.getAdresse());
        cooperative.setLicence(registrationDto.getLicence());
        cooperative.setStatutVerification("PENDING");

        return cooperativeRepository.save(cooperative);
    }

    @Override
    public Optional<Cooperative> findByUsername(String username) {
        return cooperativeRepository.findByUsername(username);
    }

    @Override
    public Cooperative verifyCooperative(UUID cooperativeId, boolean verified) {
        Cooperative cooperative = cooperativeRepository.findById(cooperativeId)
                .orElseThrow(() -> new RuntimeException("Cooperative not found"));

        cooperative.setStatutVerification(verified ? "VERIFIED" : "REJECTED");
        return cooperativeRepository.save(cooperative);
    }

    @Override
    public List<Cooperative> findAllCooperatives(Boolean verified) {
        if (verified == null) {
            return cooperativeRepository.findAll();
        }
        return cooperativeRepository.findByStatutVerification(verified ? "VERIFIED" : "PENDING");
    }

    @Override
    public Object findAllCooperatives(Boolean verified, Pageable pageable) {
        if (verified == null) {
            return cooperativeRepository.findAll(pageable).getContent();
        }
        return cooperativeRepository.findByStatutVerification(verified ? "VERIFIED" : "PENDING");
    }

    @Override
    public Object countByStatutVerification(boolean statutVerification) {
        // Since countByStatutVerification method does not exist in repository,
        // implement manually
        List<Cooperative> cooperatives = cooperativeRepository
                .findByStatutVerification(statutVerification ? "VERIFIED" : "PENDING");
        return cooperatives.size();
    }
}
