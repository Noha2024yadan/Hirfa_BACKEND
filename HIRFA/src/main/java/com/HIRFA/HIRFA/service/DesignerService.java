package com.HIRFA.HIRFA.service;

import com.HIRFA.HIRFA.dto.DesignerDTO;
import com.HIRFA.HIRFA.dto.DesignerRegistrationDto;
import com.HIRFA.HIRFA.entity.Designer;
import com.HIRFA.HIRFA.repository.DesignerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class DesignerService {

    private final DesignerRepository designerRepository;
    private final PasswordEncoder passwordEncoder;

    public DesignerService(DesignerRepository designerRepository, PasswordEncoder passwordEncoder) {
        this.designerRepository = designerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Designer registerDesigner(DesignerRegistrationDto registrationDto) {
        Designer designer = new Designer();
        designer.setNom(registrationDto.getNom());
        designer.setPrenom(registrationDto.getPrenom());
        designer.setEmail(registrationDto.getEmail());
        designer.setUsername(registrationDto.getUsername());
        designer.setTelephone(registrationDto.getTelephone());
        designer.setMotDePasse(passwordEncoder.encode(registrationDto.getMotDePasse()));
        designer.setPortfolio(registrationDto.getPortfolio());
        designer.setSpecialites(registrationDto.getSpecialites());
        designer.setUserType(com.HIRFA.HIRFA.entity.UserType.DESIGNER);
        designer.setEnabled(true);
        designer.setDateCreation(LocalDateTime.now());
        designer.setDerniereConnexion(LocalDateTime.now());

        return designerRepository.save(designer);
    }

    public Optional<Designer> findByUsername(String username) {
        return designerRepository.findByUsername(username);
    }

    public Designer updateDesignerProfile(UUID designerId, DesignerDTO dto) {
        return updateDesigner(designerId, dto);
    }

    public Page<Designer> findAllDesigners(String specialite, Pageable pageable) {
        if (specialite != null && !specialite.isEmpty()) {
            return designerRepository.findBySpecialitesContaining(specialite, pageable);
        }
        return designerRepository.findAll(pageable);
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
