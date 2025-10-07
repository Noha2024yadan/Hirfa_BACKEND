package com.HIRFA.HIRFA.service;

import com.HIRFA.HIRFA.dto.CooperativeDTO;
import com.HIRFA.HIRFA.dto.DesignerBasicDTO;
import com.HIRFA.HIRFA.entity.Cooperative;
import com.HIRFA.HIRFA.entity.Designer;
import com.HIRFA.HIRFA.repository.CooperativeRepository;
import com.HIRFA.HIRFA.repository.DesignerRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CooperativeService {

    private final CooperativeRepository cooperativeRepository;
    private final DesignerRepository designerRepository;

    public CooperativeService(CooperativeRepository cooperativeRepository,
            DesignerRepository designerRepository) {
        this.cooperativeRepository = cooperativeRepository;
        this.designerRepository = designerRepository;
    }

    public Cooperative updateCooperative(UUID cooperativeId, CooperativeDTO dto) {
        Cooperative existingCoop = cooperativeRepository.findById(cooperativeId)
                .orElseThrow(() -> new RuntimeException("Cooperative introuvable"));

        if (dto.getEmail() != null && !dto.getEmail().equals(existingCoop.getEmail())) {
            if (cooperativeRepository.existsByEmail(dto.getEmail())) {
                throw new RuntimeException("Email déjà utilisé");
            }
            existingCoop.setEmail(dto.getEmail());
        }

        if (dto.getBrand() != null && !dto.getBrand().equals(existingCoop.getBrand())) {
            if (cooperativeRepository.existsByBrand(dto.getBrand())) {
                throw new RuntimeException("Marque déjà utilisée");
            }
            existingCoop.setBrand(dto.getBrand());
        }

        if (dto.getTelephone() != null)
            existingCoop.setTelephone(dto.getTelephone());

        if (dto.getMotDePasse() != null)
            existingCoop.setMotDePasse(dto.getMotDePasse());

        if (dto.getAdresse() != null)
            existingCoop.setAdresse(dto.getAdresse());

        if (dto.getDescription() != null)
            existingCoop.setDescription(dto.getDescription());

        if (dto.getLicence() != null)
            existingCoop.setLicence(dto.getLicence());

        return cooperativeRepository.save(existingCoop);
    }

    public DesignerBasicDTO getDesignerProfile(UUID designerId) {
        Designer designer = designerRepository.findById(designerId)
                .orElseThrow(() -> new RuntimeException("Designer introuvable"));

        DesignerBasicDTO dto = new DesignerBasicDTO();
        dto.setNom(designer.getNom());
        dto.setPrenom(designer.getPrenom());
        dto.setEmail(designer.getEmail());
        dto.setUsername(designer.getUsername());
        dto.setTelephone(designer.getTelephone()); // ⚠️ ton champ s'appelle `telephone`
        dto.setSpecialites(designer.getSpecialites());
        dto.setPortfolio(designer.getPortfolio());
        dto.setTarifs(designer.getTarifs());
        return dto;
    }
}
