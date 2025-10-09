package com.HIRFA.HIRFA.service;

import com.HIRFA.HIRFA.dto.ClientRegistrationDto;
import com.HIRFA.HIRFA.entity.Client;
import com.HIRFA.HIRFA.exception.EmailAlreadyExistsException;
import com.HIRFA.HIRFA.exception.ResourceNotFoundException;
import com.HIRFA.HIRFA.exception.UsernameAlreadyExistsException;
import com.HIRFA.HIRFA.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClientServiceOld {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 🔹 Register a new client
    @Transactional
    public Client register(ClientRegistrationDto registrationDto) {
        if (clientRepository.existsByEmail(registrationDto.getEmail())) {
            throw new EmailAlreadyExistsException("Email already in use");
        }

        if (clientRepository.existsByUsername(registrationDto.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already taken");
        }

        Client client = new Client();
        client.setNom(registrationDto.getNom());
        client.setPrenom(registrationDto.getPrenom());
        client.setEmail(registrationDto.getEmail());
        client.setUsername(registrationDto.getUsername());
        client.setTelephone(registrationDto.getTelephone());
        client.setMotDePasse(passwordEncoder.encode(registrationDto.getMotDePasse()));
        client.setAdresse(registrationDto.getAdresse());
        client.setDateCreation(LocalDateTime.now());
        client.setStatut(true);

        return clientRepository.save(client);
    }

    // 🔹 Login (manual check – controller uses AuthenticationManager too)
    public Optional<Client> login(String usernameOrEmail, String password) {
        Optional<Client> client = clientRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);

        if (client.isPresent() && passwordEncoder.matches(password, client.get().getMotDePasse())) {
            Client loggedInClient = client.get();
            loggedInClient.setDerniereConnexion(LocalDateTime.now());
            return Optional.of(clientRepository.save(loggedInClient));
        }
        return Optional.empty();
    }

    // 🔹 Get all clients (with optional search)
    public Page<Client> findAllClients(String search, Pageable pageable) {
        if (search != null && !search.trim().isEmpty()) {
            return clientRepository.searchClients(search, pageable);
        }
        return clientRepository.findAll(pageable);
    }

    // 🔹 Find by ID
    public Optional<Client> findById(UUID id) {
        return clientRepository.findById(id);
    }

    // 🔹 Update client
    @Transactional
    public Client updateClient(UUID id, ClientRegistrationDto clientDto) {
        Client existingClient = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));

        if (clientDto.getNom() != null) {
            existingClient.setNom(clientDto.getNom());
        }
        if (clientDto.getPrenom() != null) {
            existingClient.setPrenom(clientDto.getPrenom());
        }
        if (clientDto.getEmail() != null && !clientDto.getEmail().equals(existingClient.getEmail())) {
            if (clientRepository.existsByEmail(clientDto.getEmail())) {
                throw new EmailAlreadyExistsException("Email already in use");
            }
            existingClient.setEmail(clientDto.getEmail());
        }
        if (clientDto.getUsername() != null && !clientDto.getUsername().equals(existingClient.getUsername())) {
            if (clientRepository.existsByUsername(clientDto.getUsername())) {
                throw new UsernameAlreadyExistsException("Username already taken");
            }
            existingClient.setUsername(clientDto.getUsername());
        }
        if (clientDto.getTelephone() != null) {
            existingClient.setTelephone(clientDto.getTelephone());
        }
        if (clientDto.getAdresse() != null) {
            existingClient.setAdresse(clientDto.getAdresse());
        }
        if (clientDto.getMotDePasse() != null && !clientDto.getMotDePasse().isEmpty()) {
            existingClient.setMotDePasse(passwordEncoder.encode(clientDto.getMotDePasse()));
        }

        return clientRepository.save(existingClient);
    }

    // 🔹 Delete client
    @Transactional
    public void deleteClient(UUID id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
        clientRepository.delete(client);
    }
}
