package com.HIRFA.HIRFA.service;

import com.HIRFA.HIRFA.dto.ClientDTO;
import com.HIRFA.HIRFA.dto.ClientRegistrationDto;
import com.HIRFA.HIRFA.entity.Client;
import com.HIRFA.HIRFA.repository.ClientRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    public ClientService(ClientRepository clientRepository, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Client registerClient(ClientRegistrationDto registrationDto) {
        Client client = new Client();
        client.setNom(registrationDto.getNom());
        client.setPrenom(registrationDto.getPrenom());
        client.setEmail(registrationDto.getEmail());
        client.setUsername(registrationDto.getUsername());
        client.setTelephone(registrationDto.getTelephone());
        client.setMotDePasse(passwordEncoder.encode(registrationDto.getMotDePasse()));
        client.setAdresse(registrationDto.getAdresse());
        client.setUserType(com.HIRFA.HIRFA.entity.UserType.CLIENT);
        client.setEnabled(true);
        client.setDateCreation(LocalDateTime.now());
        client.setDerniereConnexion(LocalDateTime.now());

        return clientRepository.save(client);
    }

    public Client updateClient(UUID clientId, ClientDTO dto) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName();

        Client existingClient = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client introuvable"));

        if (!existingClient.getUsername().equals(loggedInUsername)) {
            throw new RuntimeException("Vous ne pouvez modifier que votre profil");
        }

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

    public Page<Client> findAllClients(Pageable pageable) {
        return clientRepository.findAll(pageable);
    }
}
