package com.HIRFA.HIRFA.service;

import com.HIRFA.HIRFA.dto.ClientDTO;
import com.HIRFA.HIRFA.entity.Client;
import com.HIRFA.HIRFA.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client updateClient(UUID clientId, ClientDTO dto) {
        // 🔍 Récupération du client
        Client existingClient = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client introuvable"));

        // ✅ Mise à jour partielle (PATCH)
        if (dto.getNom() != null)
            existingClient.setNom(dto.getNom());
        if (dto.getPrenom() != null)
            existingClient.setPrenom(dto.getPrenom());

        if (dto.getEmail() != null && !dto.getEmail().equals(existingClient.getEmail())) {
            if (clientRepository.existsByEmail(dto.getEmail())) {
                throw new RuntimeException("Email déjà utilisé");
            }
            existingClient.setEmail(dto.getEmail());
        }

        if (dto.getUsername() != null && !dto.getUsername().equals(existingClient.getUsername())) {
            if (clientRepository.existsByUsername(dto.getUsername())) {
                throw new RuntimeException("Username déjà utilisé");
            }
            existingClient.setUsername(dto.getUsername());
        }

        if (dto.getTelephone() != null)
            existingClient.setTelephone(dto.getTelephone());
        if (dto.getAdresse() != null)
            existingClient.setAdresse(dto.getAdresse());
        if (dto.getMotDePasse() != null)
            existingClient.setMotDePasse(dto.getMotDePasse());

        return clientRepository.save(existingClient);
    }
}
