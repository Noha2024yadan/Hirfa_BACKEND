package com.HIRFA.HIRFA.service;

import com.HIRFA.HIRFA.dto.DesignerDTO;
import com.HIRFA.HIRFA.entity.Designer;
import com.HIRFA.HIRFA.repository.DesignerRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DesignerService {

    private final DesignerRepository designerRepository;

    public DesignerService(DesignerRepository designerRepository) {
        this.designerRepository = designerRepository;
    }

    public Designer updateDesigner(UUID designerId, DesignerDTO dto) {
        
        Designer existingDesigner = designerRepository.findById(designerId)
                .orElseThrow(() -> new RuntimeException("Designer introuvable"));

        if (dto.getNom() != null)
            existingDesigner.setNom(dto.getNom());

        if (dto.getPrenom() != null)
            existingDesigner.setPrenom(dto.getPrenom());

        if (dto.getEmail() != null && !dto.getEmail().equals(existingDesigner.getEmail())) {
            if (designerRepository.existsByEmail(dto.getEmail())) {
                throw new RuntimeException("Email déjà utilisé");
            }
            existingDesigner.setEmail(dto.getEmail());
        }

        if (dto.getUsername() != null && !dto.getUsername().equals(existingDesigner.getUsername())) {
            if (designerRepository.existsByUsername(dto.getUsername())) {
                throw new RuntimeException("Username déjà utilisé");
            }
            existingDesigner.setUsername(dto.getUsername());
        }

        if (dto.getTelephone() != null)
            existingDesigner.setTelephone(dto.getTelephone());

        if (dto.getMotDePasse() != null)
            existingDesigner.setMotDePasse(dto.getMotDePasse());

        if (dto.getPortfolio() != null)
            existingDesigner.setPortfolio(dto.getPortfolio());

        if (dto.getSpecialites() != null)
            existingDesigner.setSpecialites(dto.getSpecialites());

        if (dto.getTarifs() != null)
            existingDesigner.setTarifs(dto.getTarifs());

        if (dto.getLConfirmed() != null)
            existingDesigner.setConfirmed(dto.getLConfirmed());

        return designerRepository.save(existingDesigner);
    }
}
