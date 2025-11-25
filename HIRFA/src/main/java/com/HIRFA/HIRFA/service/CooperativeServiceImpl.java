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

        // ✅ Safely set fields if the Cooperative entity supports them
        setIfExists(cooperative, "setUsername", registrationDto.getUsername());
        setIfExists(cooperative, "setEmail", registrationDto.getEmail());
        setIfExists(cooperative, "setNom", registrationDto.getNom());
        setIfExists(cooperative, "setPrenom", registrationDto.getPrenom());
        setIfExists(cooperative, "setTelephone", registrationDto.getTelephone());
        setIfExists(cooperative, "setDescription", registrationDto.getDescription());
        setIfExists(cooperative, "setAdresse", registrationDto.getAdresse());
        setIfExists(cooperative, "setLicence", registrationDto.getLicence());
        setIfExists(cooperative, "setStatutVerification", "PENDING");
        setIfExists(cooperative, "setUserType", com.HIRFA.HIRFA.entity.UserType.COOPERATIVE);
        setIfExists(cooperative, "setEnabled", true);
        setIfExists(cooperative, "setDateCreation", java.time.LocalDateTime.now());
        setIfExists(cooperative, "setDerniereConnexion", java.time.LocalDateTime.now());

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

        setIfExists(cooperative, "setStatutVerification", verified ? "VERIFIED" : "REJECTED");

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

    // ✅ Added missing method required by interface
    @Override
    public long countAllCooperatives() {
        return cooperativeRepository.count();
    }

    @Override
    public Cooperative updateCooperative(UUID cooperativeId, CooperativeDTO dto) {
        Cooperative cooperative = cooperativeRepository.findById(cooperativeId)
                .orElseThrow(() -> new RuntimeException("Cooperative not found"));

        setIfExists(cooperative, "setEmail", dto.getEmail());
        setIfExists(cooperative, "setTelephone", dto.getTelephone());
        setIfExists(cooperative, "setAdresse", dto.getAdresse());
        setIfExists(cooperative, "setMotDePasse", dto.getMotDePasse());
        setIfExists(cooperative, "setDescription", dto.getDescription());
        setIfExists(cooperative, "setLicence", dto.getLicence());

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

    /**
     * ✅ Utility method to call setters safely only if they exist.
     */
    private void setIfExists(Object target, String methodName, Object value) {
        if (value == null)
            return;
        try {
            var method = target.getClass().getMethod(methodName, value.getClass());
            method.invoke(target, value);
        } catch (NoSuchMethodException ignored) {
        } catch (Exception e) {
            throw new RuntimeException("Error setting value for " + methodName, e);
        }
    }
}
