package com.HIRFA.HIRFA.service;

import com.HIRFA.HIRFA.dto.CooperativeDTO;
import com.HIRFA.HIRFA.dto.CooperativeRegistrationDto;
import com.HIRFA.HIRFA.dto.DesignerBasicDTO;
import com.HIRFA.HIRFA.entity.Cooperative;
import com.HIRFA.HIRFA.entity.Designer;
import com.HIRFA.HIRFA.repository.CooperativeRepository;
import com.HIRFA.HIRFA.repository.DesignerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CooperativeServiceImpl implements CooperativeService {

    @Autowired
    private CooperativeRepository cooperativeRepository;

    @Autowired
    private DesignerRepository designerRepository;

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
        cooperative.setUserType(com.HIRFA.HIRFA.entity.UserType.COOPERATIVE);
        cooperative.setEnabled(true);
        cooperative.setDateCreation(java.time.LocalDateTime.now());
        cooperative.setDerniereConnexion(java.time.LocalDateTime.now());

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
    public Page<Cooperative> findAllCooperatives(Boolean verified, Pageable pageable) {
        if (verified == null) {
            return cooperativeRepository.findAll(pageable);
        }
        return cooperativeRepository.findByStatutVerification(verified ? "VERIFIED" : "PENDING", pageable);
    }

    @Override
    public long countByStatutVerification(String statutVerification) {
        return cooperativeRepository.countByStatutVerification(statutVerification);
    }

    @Override
    public Cooperative updateCooperative(UUID cooperativeId, CooperativeDTO dto) {
        Cooperative cooperative = cooperativeRepository.findById(cooperativeId)
                .orElseThrow(() -> new RuntimeException("Cooperative not found"));

        if (dto.getEmail() != null)
            cooperative.setEmail(dto.getEmail());
        if (dto.getTelephone() != null)
            cooperative.setTelephone(dto.getTelephone());
        if (dto.getAdresse() != null)
            cooperative.setAdresse(dto.getAdresse());
        if (dto.getMotDePasse() != null)
            cooperative.setMotDePasse(dto.getMotDePasse());
        if (dto.getDescription() != null)
            cooperative.setDescription(dto.getDescription());
        if (dto.getLicence() != null)
            cooperative.setLicence(dto.getLicence());

        return cooperativeRepository.save(cooperative);
    }

    @Override
    public DesignerBasicDTO getDesignerProfile(UUID designerId) {
        Designer designer = designerRepository.findById(designerId)
                .orElseThrow(() -> new RuntimeException("Designer not found"));

        DesignerBasicDTO dto = new DesignerBasicDTO();
        dto.setNom(designer.getNom());
        dto.setPrenom(designer.getPrenom());
        dto.setEmail(designer.getEmail());
        dto.setUsername(designer.getUsername());
        dto.setTelephone(designer.getTelephone());
        dto.setPortfolio(designer.getPortfolio());
        dto.setSpecialites(designer.getSpecialites());
        dto.setTarifs(designer.getTarifs());

        return dto;
    }
}
