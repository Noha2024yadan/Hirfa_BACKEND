package com.HIRFA.HIRFA.services;

import com.HIRFA.HIRFA.repository.ClientRepository;
import com.HIRFA.HIRFA.repository.DesignerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AdminService {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private DesignerRepository designerRepository;

    public void deleteClient(UUID clientId) {
        clientRepository.deleteById(clientId);
    }

    public void deleteDesigner(UUID designerId) {
        designerRepository.deleteById(designerId);
    }
}
