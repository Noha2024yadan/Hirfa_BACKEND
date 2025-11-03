package com.HIRFA.HIRFA.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.HIRFA.HIRFA.entity.Designer;
import com.HIRFA.HIRFA.repository.DesignerRepository;

@Service
public class DesignerService {
   @Autowired
    private DesignerRepository designerRepository;

    public Designer createDesigner(String nom, String email) {
        Designer designer = Designer.builder()
                .nom(nom)
                .email(email)
                .dateCreation(LocalDateTime.now())
                .statut(true)
                .build();

        return designerRepository.save(designer);
    }
    
}
